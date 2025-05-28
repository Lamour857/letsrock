package org.wj.letsrock.domain.article.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-12:35
 **/
@Data
public class ContentPostReq implements Serializable {
    /**
     * 正文内容
     */
    private String content;
}
