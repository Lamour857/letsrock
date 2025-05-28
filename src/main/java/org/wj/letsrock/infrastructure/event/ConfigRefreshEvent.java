package org.wj.letsrock.infrastructure.event;

import lombok.*;
import org.springframework.context.ApplicationEvent;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:27
 **/
@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class ConfigRefreshEvent extends ApplicationEvent {
    private String key;
    private String val;


    public ConfigRefreshEvent(Object source, String key, String value) {
        super(source);
        this.key = key;
        this.val = value;
    }
}
