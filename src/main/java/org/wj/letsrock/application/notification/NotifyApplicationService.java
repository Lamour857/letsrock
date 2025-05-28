package org.wj.letsrock.application.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.notify.NotifyStatEnum;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.notify.model.dto.NotifyMsgDTO;
import org.wj.letsrock.domain.notify.model.entity.NotifyMsgDO;
import org.wj.letsrock.infrastructure.persistence.mybatis.notify.NotifyMsgRepositoryImpl;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.domain.user.service.UserRelationService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-13:56
 **/
@Service
public class NotifyApplicationService  {
    @Autowired
    private NotifyMsgRepositoryImpl notifyMsgDao;
    @Autowired
    private UserRelationService userRelationService;

    public void saveArticleNotify(UserFootDO foot, NotifyTypeEnum notifyTypeEnum) {
        NotifyMsgDO msg = new NotifyMsgDO().setRelatedId(foot.getDocumentId())
                .setNotifyUserId(foot.getDocumentUserId())
                .setOperateUserId(foot.getUserId())
                .setType(notifyTypeEnum.getType() )
                .setState(NotifyStatEnum.UNREAD.getStat())
                .setMsg("");
        NotifyMsgDO record = notifyMsgDao.getByUserIdRelatedIdAndType(msg);
        if (record == null) {
            // 若之前已经有对应的通知，则不重复记录；因为一个用户对一篇文章，可以重复的点赞、取消点赞，但是最终我们只通知一次
            notifyMsgDao.save(msg);
        }
    }

    /**
     * 查询消息通知列表
     *
     * @return
     */

    public PageListVo<NotifyMsgDTO> queryUserNotices(Long userId, NotifyTypeEnum type, PageParam page) {
        List<NotifyMsgDTO> list = notifyMsgDao.listNotifyMsgByUserIdAndType(userId, type, page);
        if (CollectionUtils.isEmpty(list)) {
            return PageListVo.emptyVo();
        }

        // 设置消息为已读状态
        notifyMsgDao.updateNotifyMsgToRead(list);
        // 更新全局总的消息数
        RequestInfoContext.getReqInfo().setMsgNum(queryUserNotifyMsgCount(userId));
        // 更新当前登录用户对粉丝的关注状态
        updateFollowStatus(userId, list);
        return PageListVo.newVo(list, page.getPageSize());
    }

    private void updateFollowStatus(Long userId, List<NotifyMsgDTO> list) {
        List<Long> targetUserIds = list.stream().filter(s -> s.getType() == NotifyTypeEnum.FOLLOW.getType()).map(NotifyMsgDTO::getOperateUserId).collect(Collectors.toList());
        if (targetUserIds.isEmpty()) {
            return;
        }

        // 查询userId已经关注过的用户列表；并将对应的msg设置为true，表示已经关注过了；不需要再关注
        Set<Long> followedUserIds = userRelationService.getFollowedUserId(targetUserIds, userId);
        list.forEach(notify -> {
            if (followedUserIds.contains(notify.getOperateUserId())) {
                notify.setMsg("true");
            } else {
                notify.setMsg("false");
            }
        });
    }


    public int queryUserNotifyMsgCount(Long userId) {
        return notifyMsgDao.countByUserIdAndStat(userId, NotifyStatEnum.UNREAD.getStat());
    }
}
