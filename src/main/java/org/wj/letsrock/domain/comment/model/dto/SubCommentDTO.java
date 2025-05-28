package org.wj.letsrock.domain.comment.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:51
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class SubCommentDTO extends BaseCommentDTO{
    /**
     * 父评论内容
     */
    private String parentContent;


    @Override
    public int compareTo( BaseCommentDTO o) {
        return Long.compare(this.getCommentTime(), o.getCommentTime());
    }
}
