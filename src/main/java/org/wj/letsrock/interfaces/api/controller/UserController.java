package org.wj.letsrock.interfaces.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.article.ArticleApplicationService;
import org.wj.letsrock.application.user.UserApplicationService;
import org.wj.letsrock.domain.user.model.dto.SearchUserDTO;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.FollowTypeEnum;
import org.wj.letsrock.enums.HomeSelectEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.security.token.AuthenticationToken;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.user.model.dto.FollowUserInfoDTO;
import org.wj.letsrock.domain.user.model.request.UserInfoSaveReq;
import org.wj.letsrock.domain.user.model.request.UserRelationReq;
import org.wj.letsrock.domain.user.service.UserRelationService;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.utils.ExceptionUtil;

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
    /**
     * 保存用户关系
     *
     * @param req
     */
    //@Permission(role = UserRole.LOGIN)
    @PostMapping(path = "saveUserRelation")
    public ResultVo<Boolean> saveUserRelation(@RequestBody UserRelationReq req) {
        log.info("保存用户关系");
        if(Objects.equals(req.getFollowUserId(), RequestInfoContext.getReqInfo().getUserId())){
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "不能关注自己");
        }
        userService.saveUserRelation(req);
        return ResultVo.ok(true);
    }
    /**
     * 保存用户详情
     *
     * @param req
     * @return
     * @throws Exception
     */
    //@Permission(role = UserRole.LOGIN)
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
     *
     * @param userId
     * @param homeSelectType
     * @return
     */
    @GetMapping(path = "article")
    public ResultVo<PageListVo<ArticleDTO>> articleList(@RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "homeSelectType") String homeSelectType,
                                             @RequestParam("page") Long page,
                                             @RequestParam(name = "pageSize", required = false) Long pageSize) {
        HomeSelectEnum select = HomeSelectEnum.fromCode(homeSelectType);
        if (select == null) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }

        if (pageSize == null) pageSize = PageParam.DEFAULT_PAGE_SIZE;
        PageParam pageParam = PageParam.newPageInstance(page, pageSize);
        PageListVo<ArticleDTO> dto = articleService.queryArticlesByUserAndType(userId, pageParam, select);

        return ResultVo.ok(dto);
    }

    /**
     * 获取用户关注列表
     */
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
    @GetMapping("info")
    public ResultVo<UserDO> info() {
        UserDO user =((AuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getUser();
        return ResultVo.ok(user);
    }

    @ApiOperation("用户搜索")
    @GetMapping(path = "query")
    public ResultVo<SearchUserDTO> queryUserList(@RequestParam(name = "key", required = false) String key) {

        SearchUserDTO vo = userService.queryUserList(key);
        return ResultVo.ok(vo);
    }
}
