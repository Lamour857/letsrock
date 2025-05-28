package org.wj.letsrock.domain.article.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:19
 **/
@Data
@ApiModel("教程排序，根据 ID 和新填的排序")
public class SortColumnArticleByIdReq implements Serializable {
    // 要排序的 id
    @ApiModelProperty("要排序的 id")
    private Long id;
    // 新的排序
    @ApiModelProperty("新的排序")
    private Integer sort;
}
