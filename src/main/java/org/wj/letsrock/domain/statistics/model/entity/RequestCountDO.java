package org.wj.letsrock.domain.statistics.model.entity;

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
 * 请求计数表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@ApiModel(value="Request_count对象", description="请求计数表")
@TableName("request_count")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class RequestCountDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "机器IP")
    private String host;

    @ApiModelProperty(value = "访问计数")
    private Integer cnt;

    @ApiModelProperty(value = "当前日期")
    private Date date;

}
