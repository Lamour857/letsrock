package org.wj.letsrock.domain.article.model.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 专栏
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("column_info")
@ToString
@ApiModel(value="Column_info对象", description="专栏")
public class ColumnInfoDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专栏名")
    private String columnName;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "专栏简述")
    private String introduction;

    @ApiModelProperty(value = "专栏封面")
    private String cover;

    @ApiModelProperty(value = "状态: 0-审核中，1-连载，2-完结")
    private Integer state;

    @ApiModelProperty(value = "上线时间")
    private Date publishTime;

    @ApiModelProperty(value = "排序")
    private Integer section;

    @ApiModelProperty(value = "专栏预计的更新的文章数")
    private Integer nums;

    @ApiModelProperty(value = "专栏类型 0-免费 1-登录阅读 2-限时免费 3-星球")
    private Integer type;

    @ApiModelProperty(value = "限时免费开始时间")
    private Date freeStartTime;

    @ApiModelProperty(value = "限时免费结束时间")
    private Date freeEndTime;



}
