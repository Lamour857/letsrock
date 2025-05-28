package org.wj.letsrock.infrastructure.persistence.mybatis.article;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.article.model.entity.ArticlePraiseDO;
import org.wj.letsrock.domain.article.repository.ArticlePraiseRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.ArticlePraiseMapper;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-26-15:54
 **/
@Repository
public class ArticlePraiseRepositoryImpl extends ServiceImpl<ArticlePraiseMapper, ArticlePraiseDO> implements ArticlePraiseRepository {
    @Override
    public ArticlePraiseDO findByArticleIdAndUserId(Long articleId, Long userId) {
        LambdaQueryWrapper<ArticlePraiseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticlePraiseDO::getArticleId, articleId);
        queryWrapper.eq(ArticlePraiseDO::getUserId, userId);
        return baseMapper.selectOne(queryWrapper);
    }
}
