package org.wj.letsrock.domain.notify.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 消息通知列表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Notify_msg对象", description="消息通知列表")
@Data
@TableName("notify_msg")
@ToString
@Accessors(chain = true)
public class NotifyMsgDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联的主键")
    private Long relatedId;

    @ApiModelProperty(value = "通知的用户id")
    private Long notifyUserId;

    @ApiModelProperty(value = "触发这个通知的用户id")
    private Long operateUserId;

    @ApiModelProperty(value = "消息内容")
    private String msg;

    @ApiModelProperty(value = "类型: 0-默认，1-评论，2-回复 3-点赞 4-收藏 5-关注 6-系统")
    private Integer type;

    @ApiModelProperty(value = "阅读状态: 0-未读，1-已读")
    private Integer state;

}
