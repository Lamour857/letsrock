package org.wj.letsrock.domain.article.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:27
 **/
@Data
@ApiModel("教程配套文章查询")
public class SearchColumnArticleReq {

    // 教程名称
    @ApiModelProperty("教程名称")
    private String column;

    // 教程 ID
    @ApiModelProperty("教程 ID")
    private Long columnId;

    // 文章标题
    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("请求页数，从1开始计数")
    private long pageNumber;

    @ApiModelProperty("请求页大小，默认为 10")
    private long pageSize;
}
