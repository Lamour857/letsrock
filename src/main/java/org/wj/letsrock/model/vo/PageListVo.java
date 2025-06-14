package org.wj.letsrock.model.vo;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:24
 **/
@Data
public class PageListVo<T> {
    List<T> list;

    /**
     * 是否有更多
     */
    private Boolean hasMore;

    public static <T> PageListVo<T> emptyVo() {
        PageListVo<T> vo = new PageListVo<>();
        vo.setList(Collections.emptyList());
        vo.setHasMore(false);
        return vo;
    }

    public static <T> PageListVo<T> newVo(List<T> list, long pageSize) {
        PageListVo<T> vo = new PageListVo<>();
        vo.setList(Optional.ofNullable(list).orElse(Collections.emptyList()));
        vo.setHasMore(vo.getList().size() == pageSize);
        return vo;
    }
}
