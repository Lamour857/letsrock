package org.wj.letsrock.infrastructure.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import org.wj.letsrock.enums.article.ArticleEventEnum;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:33
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public  class ArticleMsgEvent<T> extends ApplicationEvent {
    private ArticleEventEnum type;

    private T content;


    public ArticleMsgEvent(Object source, ArticleEventEnum type, T content) {
        super(source);
        this.type = type;
        this.content = content;
    }
}
