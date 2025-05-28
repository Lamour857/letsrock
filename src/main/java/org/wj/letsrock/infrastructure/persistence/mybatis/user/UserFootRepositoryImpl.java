package org.wj.letsrock.infrastructure.persistence.mybatis.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.user.repository.UserFootRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.mapper.UserFootMapper;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.enums.article.PraiseStatEnum;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.UserFootStatisticDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-22:43
 **/
@Repository
public class UserFootRepositoryImpl extends ServiceImpl<UserFootMapper, UserFootDO> implements UserFootRepository {
    @Override
    public UserFootDO getByDocumentAndUserId(Long documentId, Integer type, Long userId) {
        LambdaQueryWrapper<UserFootDO> query = Wrappers.lambdaQuery();
        query.eq(UserFootDO::getDocumentId, documentId)
                .eq(UserFootDO::getDocumentType, type)
                .eq(UserFootDO::getUserId, userId);
        return baseMapper.selectOne(query);
    }
    @Override
    public List<SimpleUserInfoDTO> listDocumentPraisedUsers(Long articleId, Integer type, int size) {
        return baseMapper.listSimpleUserInfosByArticleId(articleId, type, size);
    }

    /**
     * 查询评论的点赞数
     *
     * @param commentId
     * @return
     */
    @Override
    public Long countCommentPraise(Long commentId) {
        return lambdaQuery()
                .eq(UserFootDO::getDocumentId, commentId)
                .eq(UserFootDO::getDocumentType, DocumentTypeEnum.COMMENT.getCode())
                .eq(UserFootDO::getPraiseStat, PraiseStatEnum.PRAISE.getCode())
                .count();
    }

    /**
     * 查询用户阅读的文章列表
     *
     * @param userId 用户id
     * @param pageParam 页参数
     * @return
     */
    @Override
    public List<Long> listReadArticleByUserId(Long userId, PageParam pageParam) {
        return baseMapper.listReadArticleByUserId(userId, pageParam);
    }
    /**
     * 查询用户收藏的文章列表
     *
     * @param userId 用户id
     * @param pageParam 页参数
     * @return
     */
    @Override
    public List<Long> listCollectedArticlesByUserId(Long userId, PageParam pageParam) {
        return baseMapper.listCollectedArticlesByUserId(userId, pageParam);
    }
    @Override
    public UserFootStatisticDTO getFootCount() {
        return baseMapper.getFootCount();
    }
}
