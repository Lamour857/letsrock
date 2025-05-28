package org.wj.letsrock.domain.config.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 配置表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@ApiModel(value="Config对象", description="配置表")
@Data
@TableName("config")
public class ConfigDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;



    @ApiModelProperty(value = "配置类型：1-首页，2-侧边栏，3-广告位，4-公告")
    private Integer type;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "图片链接")
    private String bannerUrl;

    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "排序")
    private Integer rank;

    @ApiModelProperty(value = "状态：0-未发布，1-已发布")
    private Integer status;

    @ApiModelProperty(value = "配置关联标签，英文逗号分隔 1 火 2 官方 3 推荐")
    private String tags;

    @ApiModelProperty(value = "扩展信息")
    private String extra;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;



}
