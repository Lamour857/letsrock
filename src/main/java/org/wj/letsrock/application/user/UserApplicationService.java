package org.wj.letsrock.application.user;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.wj.letsrock.application.image.ImageService;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.user.model.dto.*;
import org.wj.letsrock.domain.user.model.entity.UserInfoDO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;
import org.wj.letsrock.domain.user.model.request.UserInfoSaveReq;
import org.wj.letsrock.domain.user.model.request.UserRelationReq;
import org.wj.letsrock.domain.user.service.UserActivityRankService;
import org.wj.letsrock.domain.user.service.UserRelationService;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.enums.ActivityRankTimeEnum;
import org.wj.letsrock.enums.FollowTypeEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.utils.ExceptionUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-22-22:24
 **/
@Service
@Slf4j
public class UserApplicationService {
    @Autowired
    private UserActivityRankService userActivityRankService;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ArticleReadService articleReadService;
    public RankInfoDTO queryUserRank(String time) {
        ActivityRankTimeEnum rankTime = ActivityRankTimeEnum.nameOf(time);
        if (rankTime == null) {
            rankTime = ActivityRankTimeEnum.MONTH;
        }
        List<RankItemDTO> list = userActivityRankService.queryRankList(rankTime, 30);
        RankInfoDTO info = new RankInfoDTO();
        info.setItems(list);
        info.setTime(rankTime);
        return info;
    }

    public void saveUserRelation(UserRelationReq req) {

        userRelationService.saveUserRelation(req);
    }

    public void saveUserInfo(UserInfoSaveReq req) {
        userService.saveUserInfo(req);
    }
    public PageListVo<FollowUserInfoDTO> getFollowList(String followSelectType, Long userId, PageParam pageParam){
        PageListVo<FollowUserInfoDTO> followList;
        boolean needUpdateRelation = false;
        if (followSelectType.equals(FollowTypeEnum.FOLLOW.getCode())) {
            followList = userRelationService.getUserFollowList(userId, pageParam);
        } else {
            // 查询粉丝列表时，只能确定粉丝关注了userId，但是不能反向判断，因此需要再更新下映射关系，判断userId是否有关注这个用户
            followList = userRelationService.getUserFansList(userId, pageParam);
            needUpdateRelation = true;
        }

        Long loginUserId = RequestInfoContext.getReqInfo().getUserId();
        if (!Objects.equals(loginUserId, userId) || needUpdateRelation) {
            userRelationService.updateUserFollowRelationId(followList, userId);
        }
        return followList;
    }

    public SearchUserDTO queryUserList(@RequestParam(name = "key", required = false) String key) {
        SearchUserDTO searchUserDTO = new SearchUserDTO();
        List<SimpleUserInfoDTO> list = userService.searchUser(key);
        searchUserDTO.setKey(key);
        searchUserDTO.setItems(list);
        return searchUserDTO;
    }
    @Transactional
    public void saveAvatar(String imageUrl) {
        UserInfoDO userInfoDO = userService.getByUserId(RequestInfoContext.getReqInfo().getUserId());
        String avatar=userInfoDO.getAvatar();
        log.info("用户:{} 保存头像，原头像为：{}",userInfoDO.getUserName(),avatar);
        if(StringUtil.isNullOrEmpty(avatar)){
            // 原头像为空
            userService.saveAvatar(userInfoDO, imageUrl);
            return;
        }
        if(imageService.isImageExist(avatar)){
            // 原头像不为空且存在
            try {
                imageService.deleteImage(avatar);
            } catch (IOException e) {
                log.warn("删除图片失败",e);
            }
        }else{
            userService.saveAvatar(userInfoDO, imageUrl);
        }
    }

    public BaseUserInfoDTO queryUserInfo(Long userId) {
        return userService.queryBasicUserInfo(userId);
    }

    public UserStatisticInfoDTO queryUserStatisticInfo(Long userId) {
        UserStatisticInfoDTO userStatisticInfoDTO = new UserStatisticInfoDTO();
        if(userId==null){
            userId = RequestInfoContext.getReqInfo().getUserId();
        }
        List<UserRelationDO> relationList = userRelationService.getRelatedRelations(userId);
        int followCount = 0;
        int fansCount = 0;
        if(relationList!=null){
            for(UserRelationDO relationDO:relationList){
                if(Objects.equals(relationDO.getUserId(), userId)){
                    followCount++;
                }
                if(Objects.equals(relationDO.getFollowUserId(), userId)){
                    fansCount++;
                }
            }
        }
        userStatisticInfoDTO.setFollowCount(followCount);
        userStatisticInfoDTO.setFansCount(fansCount);
        userStatisticInfoDTO.setArticleCount(articleReadService.getArticleCount());
        userStatisticInfoDTO.setJoinDayCount(userService.getJoinDayCount(userId));
        return userStatisticInfoDTO;
    }
}
