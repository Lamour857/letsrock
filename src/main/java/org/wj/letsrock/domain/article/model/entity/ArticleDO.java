package org.wj.letsrock.domain.article.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wj.letsrock.domain.common.Collectable;
import org.wj.letsrock.domain.common.Praiseable;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Article对象", description="文章表")
@Data
@ToString
@Accessors(chain = true)
@TableName("article")
public class ArticleDO extends BaseDO implements Serializable, Praiseable, Collectable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "文章类型：1-博文，2-问答")
    private Integer articleType;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "短标题")
    private String shortTitle;

    @ApiModelProperty(value = "文章头图")
    private String picture;

    @ApiModelProperty(value = "文章摘要")
    private String summary;

    @ApiModelProperty(value = "类目ID")
    private Long categoryId;

    @ApiModelProperty(value = "来源：1-转载，2-原创，3-翻译")
    private Integer source;

    @ApiModelProperty(value = "原文链接")
    private String sourceUrl;

    @ApiModelProperty(value = "官方状态：0-非官方，1-官方")
    private Integer officialStat;

    @ApiModelProperty(value = "置顶状态：0-不置顶，1-置顶")
    private Integer toppingStat;

    @ApiModelProperty(value = "点赞数")
    private Integer praise;

     @ApiModelProperty(value = "阅读数")
     private Integer readCount;

    @ApiModelProperty(value = "收藏数")
    private Integer collection;

    @ApiModelProperty(value = "评论数")
     private Integer comment;

    @ApiModelProperty(value = "加精状态：0-不加精，1-加精")
    private Integer creamStat;

    @ApiModelProperty(value = "状态：0-未发布，1-已发布")
    private Integer status;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;

    @ApiModelProperty(value = "文章阅读类型 0-直接阅读 1-登录阅读 2-付费阅读 3-星球")
    private Integer readType;


    @Override
    public Integer praise() {
        this.setPraise( this.getPraise()+1);
         return this.getPraise();
    }

    @Override
    public Integer cancelPraise() {
        this.setPraise( Math.max(this.getPraise() - 1, 0));
         return this.getPraise();
    }

    @Override
    public Integer collect() {
        this.setCollection( this.getCollection()+1);
        return this.getCollection();
    }

    @Override
    public Integer cancelCollect() {
        this.setCollection(Math.max(this.getCollection()-1,0));
        return this.getCollection();
    }
}
