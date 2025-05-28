package org.wj.letsrock.infrastructure.persistence.mybatis.article;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.article.repository.ColumnArticleRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.ColumnArticleMapper;
import org.wj.letsrock.domain.article.model.entity.ColumnArticleDO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:43
 **/
@Repository
public class ColumnArticleRepositoryImpl extends ServiceImpl<ColumnArticleMapper, ColumnArticleDO> implements ColumnArticleRepository {
    /**
     * 返回专栏最大更新章节数
     *
     * @param columnId
     * @return 专栏内无文章时，返回0；否则返回当前最大的章节数
     */
    @Override
    public int selectMaxSection(Long columnId) {
        return baseMapper.selectMaxSection(columnId);
    }
    @Override
    public ColumnArticleDO selectBySection(Long columnId, Integer sort) {
        return lambdaQuery()
                .eq(ColumnArticleDO::getColumnId, columnId)
                .eq(ColumnArticleDO::getSection, sort)
                .one();
    }
}
