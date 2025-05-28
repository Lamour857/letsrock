package org.wj.letsrock.domain.article.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:28
 **/
@Data
@ApiModel(value="专栏信息")
public class SearchColumnDTO implements Serializable {
    private static final long serialVersionUID = -2989169905031769195L;

    @ApiModelProperty("搜索的关键词")
    private String key;

    @ApiModelProperty("专栏列表")
    private List<SimpleColumnDTO> items;
}