package org.wj.letsrock.domain.notify.repository;

import org.wj.letsrock.domain.notify.model.dto.NotifyMsgDTO;
import org.wj.letsrock.domain.notify.model.entity.NotifyMsgDO;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.model.vo.PageParam;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-17:19
 **/
public interface NotifyMsgRepository {
    NotifyMsgDO getByUserIdRelatedIdAndType(NotifyMsgDO msg);

    List<NotifyMsgDTO> listNotifyMsgByUserIdAndType(long userId, NotifyTypeEnum type, PageParam page);

    void updateNotifyMsgToRead(List<NotifyMsgDTO> list);

    int countByUserIdAndStat(Long userId, Integer stat);
}
