package org.wj.letsrock.domain.article.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-10:44
 **/
@Data
@ApiModel(value="文章信息")
public class SearchArticleDTO implements Serializable {
    private static final long serialVersionUID = -2989169905031769195L;

    @ApiModelProperty("搜索的关键词")
    private String key;

    @ApiModelProperty("文章列表")
    private List<SimpleArticleDTO> items;
}
