package org.wj.letsrock.domain.article.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-10:44
 **/
@Data
@Accessors(chain = true)
public class SimpleArticleDTO implements Serializable {
    private static final long serialVersionUID = 3646376715620165839L;

    @ApiModelProperty("文章ID")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("专栏ID")
    private Long columnId;

    @ApiModelProperty("专栏标题")
    private String column;

    @ApiModelProperty("文章排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Timestamp createTime;

    /**
     * @see ColumnArticleReadEnum#getRead()
     */
    @ApiModelProperty("阅读模式")
    private Integer readType;
}
