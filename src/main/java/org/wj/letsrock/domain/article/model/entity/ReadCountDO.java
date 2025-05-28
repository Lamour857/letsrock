package org.wj.letsrock.domain.article.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 计数表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Read_count对象", description="计数表")
@Data
@TableName("read_count")
@ToString
@NoArgsConstructor
public class ReadCountDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "文档ID（文章/评论）")
    private Long documentId;

    @ApiModelProperty(value = "文档类型：1-文章，2-评论")
    private Integer documentType;

    @ApiModelProperty(value = "访问计数")
    private Integer cnt;

    public ReadCountDO(Long documentId, Integer documentType, Integer cnt) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.cnt = cnt;
    }

}
