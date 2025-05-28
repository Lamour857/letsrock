package org.wj.letsrock.domain.article.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:18
 **/
@Data
@ApiModel("教程排序")
public class SortColumnArticleReq implements Serializable {
    // 排序前的文章 ID
    @ApiModelProperty("排序前的文章 ID")
    private Long activeId;

    // 排序后的文章 ID
    @ApiModelProperty("排序后的文章 ID")
    private Long overId;
}
