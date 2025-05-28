package org.wj.letsrock.domain.comment.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:49
 **/
@Data
public class TopCommentDTO extends BaseCommentDTO{
    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 子评论
     */
    private List<SubCommentDTO> childComments;

    public List<SubCommentDTO> getChildComments() {
        if (childComments == null) {
            childComments = new ArrayList<>();
        }
        return childComments;
    }

    @Override
    public int compareTo( BaseCommentDTO o) {
        return Long.compare(o.getCommentTime(), this.getCommentTime());
    }
}
