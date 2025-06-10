package org.wj.letsrock.interfaces.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wj.letsrock.application.article.ArticleApplicationService;
import org.wj.letsrock.application.image.ImageService;
import org.wj.letsrock.application.user.UserApplicationService;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.SearchUserDTO;
import org.wj.letsrock.domain.user.model.dto.UserStatisticInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.FollowTypeEnum;
import org.wj.letsrock.enums.HomeSelectEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.security.annotation.AnonymousAccess;
import org.wj.letsrock.infrastructure.security.token.AuthenticationToken;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.user.model.dto.FollowUserInfoDTO;
import org.wj.letsrock.domain.user.model.request.UserInfoSaveReq;
import org.wj.letsrock.domain.user.model.request.UserRelationReq;
import org.wj.letsrock.domain.user.service.UserRelationService;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.utils.ImageUtil;
import org.wj.letsrock.utils.StopWatchUtil;

import java.util.Objects;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-12:21
 **/
@RestController
@RequestMapping(path = "user")
@Slf4j
public class UserController {

    @Autowired
    private UserApplicationService userService;
    @Autowired
    private ArticleApplicationService articleService;
    @Autowired
    private ImageService imageService;
    /**
     * 保存用户关系
     *
     * @param req
     */
    @PreAuthorize("hasRole('user')")
    @PostMapping(path = "saveUserRelation")
    public ResultVo<Boolean> saveUserRelation(@RequestBody UserRelationReq req) {
        log.info("保存用户关系");
        if(Objects.equals(req.getFollowUserId(), RequestInfoContext.getReqInfo().getUserId())){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "不能关注自己");
        }
        userService.saveUserRelation(req);
        return ResultVo.ok(true);
    }
    @PreAuthorize("hasRole('user')")
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVo<String> uploadImage(  @RequestParam("file") MultipartFile file){
        StopWatchUtil stopWatch = new StopWatchUtil("上传图片");
        stopWatch.start("参数校验");
        if (file.isEmpty()) {
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED, "图片文件为空");
        }
        ImageUtil.validateImageFile(file);
        stopWatch.stop();

        stopWatch.start("保存图片");
        String imageUrl= imageService.saveImg(file);
        userService.saveAvatar(imageUrl);
        stopWatch.stop();
        stopWatch.prettyPrint();
        return ResultVo.ok(imageUrl);
    }
    /**
     * 保存用户详情
     */
    @PreAuthorize("hasRole('user')")
    @PostMapping(path = "saveUserInfo")
    @Transactional(rollbackFor = Exception.class)
    public ResultVo<Boolean> saveUserInfo(@RequestBody UserInfoSaveReq req) {
        if (req.getUserId() == null || !Objects.equals(req.getUserId(), RequestInfoContext.getReqInfo().getUserId())) {
            // 不能修改其他用户的信息
            return ResultVo.fail(StatusEnum.FORBID_ERROR_MIXED, "无权修改");
        }
        userService.saveUserInfo(req);
        return ResultVo.ok(true);
    }
    /**
     * 用户的文章列表翻页
     * todo  翻页优化
     */
    @PreAuthorize("hasRole('user')")
    @GetMapping(path = "article")
    public ResultVo<PageResultVo<ArticleDTO>> articleList(@RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "homeSelectType") String homeSelectType,
                                             @RequestParam("page") Long page,
                                             @RequestParam(name = "pageSize", required = false) Long pageSize) {
        HomeSelectEnum select = HomeSelectEnum.fromCode(homeSelectType);
        if (select == null) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }

        if (pageSize == null) pageSize = PageParam.DEFAULT_PAGE_SIZE;
        PageParam pageParam = PageParam.newPageInstance(page, pageSize);
        PageResultVo<ArticleDTO> dto = articleService.queryArticlesByUserAndType(userId, pageParam, select);

        return ResultVo.ok(dto);
    }

    /**
     * 获取用户关注列表
     */
    @PreAuthorize("hasRole('user')")
    @GetMapping(path = "follow")
    public ResultVo<PageListVo<FollowUserInfoDTO>> followList(@RequestParam(name = "userId") Long userId,
                                            @RequestParam(name = "followSelectType") String followSelectType,
                                            @RequestParam("page") Long page,
                                            @RequestParam(name = "pageSize", required = false) Long pageSize) {
        if (pageSize == null) pageSize = PageParam.DEFAULT_PAGE_SIZE;
        PageParam pageParam = PageParam.newPageInstance(page, pageSize);
        PageListVo<FollowUserInfoDTO> list= userService.getFollowList(followSelectType, userId, pageParam);
        return ResultVo.ok(list);
    }

    @ApiOperation("获取当前登录用户信息")
    @PreAuthorize("hasRole('user')")
    @GetMapping("info")
    public ResultVo<BaseUserInfoDTO> info() {
        BaseUserInfoDTO user = userService.queryUserInfo(RequestInfoContext.getReqInfo().getUserId());
        return ResultVo.ok(user);
    }

    @ApiOperation("用户搜索")
    @AnonymousAccess
    @GetMapping(path = "query")
    public ResultVo<SearchUserDTO> queryUserList(@RequestParam(name = "key", required = false) String key) {

        SearchUserDTO vo = userService.queryUserList(key);
        return ResultVo.ok(vo);
    }

    @ApiOperation("用户基本数据")
    @AnonymousAccess
    @GetMapping(path = "statistic")
    public ResultVo<UserStatisticInfoDTO> queryUserStatisticInfo(@RequestParam(name = "userId" ,required = false) Long userId) {
        UserStatisticInfoDTO vo = userService.queryUserStatisticInfo(userId);
        return ResultVo.ok(vo);
    }
}
