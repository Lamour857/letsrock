package org.wj.letsrock.infrastructure.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-14:16
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class NotifyMsgEvent<T> extends ApplicationEvent {

    private NotifyTypeEnum notifyType;

    private T content;


    public NotifyMsgEvent(Object source, NotifyTypeEnum notifyType, T content) {
        super(source);
        this.notifyType = notifyType;
        this.content = content;
    }


}
