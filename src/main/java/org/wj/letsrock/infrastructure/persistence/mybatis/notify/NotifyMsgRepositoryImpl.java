package org.wj.letsrock.infrastructure.persistence.mybatis.notify;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.notify.repository.NotifyMsgRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.notify.mapper.NotifyMsgMapper;
import org.wj.letsrock.enums.notify.NotifyStatEnum;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.notify.model.dto.NotifyMsgDTO;
import org.wj.letsrock.domain.notify.model.entity.NotifyMsgDO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-14:08
 **/
@Repository
public class NotifyMsgRepositoryImpl extends ServiceImpl<NotifyMsgMapper, NotifyMsgDO> implements NotifyMsgRepository {
    @Override
    public NotifyMsgDO getByUserIdRelatedIdAndType(NotifyMsgDO msg) {
       LambdaQueryWrapper<NotifyMsgDO> queryWrapper = Wrappers.lambdaQuery();
       queryWrapper.eq(NotifyMsgDO::getNotifyUserId, msg.getNotifyUserId())
               .eq(NotifyMsgDO::getRelatedId, msg.getRelatedId())
               .eq(NotifyMsgDO::getType, msg.getType());
       return getOne(queryWrapper);
    }

    /**
     * 查询用户消息列表
     *
     * @param userId
     * @param type
     * @return
     */
    @Override
    public List<NotifyMsgDTO> listNotifyMsgByUserIdAndType(long userId, NotifyTypeEnum type, PageParam page) {
        switch (type) {
            case REPLY:
            case COMMENT:
            case COLLECT:
            case PRAISE:
                return baseMapper.listArticleRelatedNotices(userId, type.getType(), page);
            default:
                return baseMapper.listNormalNotices(userId, type.getType(), page);
        }
    }
    @Override
    public void updateNotifyMsgToRead(List<NotifyMsgDTO> list) {
        List<Long> ids = list.stream().filter(s -> s.getState() == NotifyStatEnum.UNREAD.getStat()).map(NotifyMsgDTO::getMsgId).collect(Collectors.toList());
        if (!ids.isEmpty()) {
            baseMapper.updateNoticeRead(ids);
        }
    }

    /**
     * 查询用户的消息通知数量
     *
     * @param userId
     * @return
     */
    @Override
    public int countByUserIdAndStat(Long userId, Integer stat) {
        return lambdaQuery()
                .eq(NotifyMsgDO::getNotifyUserId, userId)
                .eq(stat != null, NotifyMsgDO::getState, stat)
                .count().intValue();
    }
}
