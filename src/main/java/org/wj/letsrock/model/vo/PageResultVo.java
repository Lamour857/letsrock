package org.wj.letsrock.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-12:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResultVo<T> {
    private List<T> list;

    private long pageSize;

    private long pageNum;

    private long pageTotal;

    private long total;



    /**
     * 构造PageVO
     */
    public PageResultVo(List<T> list, long pageSize, long pageNum, long total) {
        this.list = list;
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.pageTotal = (long) Math.ceil((double) total / pageSize);
    }
    /**
     * 创建PageVO
     */
    public static <T> PageResultVo<T> build(List<T> list, long pageSize, long pageNum, long total) {
        return new PageResultVo<>(list, pageSize, pageNum, total);
    }
}
