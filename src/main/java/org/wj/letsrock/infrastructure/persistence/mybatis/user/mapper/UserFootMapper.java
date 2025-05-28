package org.wj.letsrock.infrastructure.persistence.mybatis.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.UserFootStatisticDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-22:44
 **/
public interface UserFootMapper extends BaseMapper<UserFootDO> {
    /**
     * 查询文章的点赞列表
     */
    List<SimpleUserInfoDTO> listSimpleUserInfosByArticleId(@Param("documentId")Long documentId,
                                                          @Param("type") Integer type,
                                                          @Param("size") int size);
    /**
     * 查询用户阅读的文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    List<Long> listReadArticleByUserId(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);

    /**
     * 查询用户收藏的文章列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    List<Long> listCollectedArticlesByUserId(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);

    UserFootStatisticDTO getFootCount();
}
