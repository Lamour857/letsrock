package org.wj.letsrock.infrastructure.persistence.mybatis.article;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.article.model.dto.ColumnArticleDTO;
import org.wj.letsrock.domain.article.repository.ColumnRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.ColumnArticleMapper;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.ColumnInfoMapper;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.article.model.entity.ColumnArticleDO;
import org.wj.letsrock.domain.article.model.entity.ColumnInfoDO;
import org.wj.letsrock.domain.article.model.param.SearchColumnArticleParams;
import org.wj.letsrock.domain.article.model.param.SearchColumnParams;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:51
 **/
@Repository
public class ColumnRepositoryImpl extends ServiceImpl<ColumnInfoMapper, ColumnInfoDO> implements ColumnRepository {
    @Autowired
    private ColumnArticleMapper columnArticleMapper;

    // TODO 改为逻辑删除
    @Override
    public void deleteColumn(Long columnId) {
        ColumnInfoDO columnInfoDO = baseMapper.selectById(columnId);
        if (columnInfoDO != null) {
            // 如果专栏对应的文章不为空，则不允许删除
            // 统计专栏的文章数
            int count = countColumnArticles(columnId);
            if (count > 0) {
                throw ExceptionUtil.of(StatusEnum.COLUMN_ARTICLE_EXISTS,"请先删除教程");
            }

            // 删除专栏
            baseMapper.deleteById(columnId);
        }
    }
    @Override
    public Long countColumnArticles() {
        return columnArticleMapper.selectCount(Wrappers.emptyWrapper());
    }

    /**
     * 统计专栏的文章数
     *
     * @return
     */
    @Override
    public int countColumnArticles(Long columnId) {
        LambdaQueryWrapper<ColumnArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ColumnArticleDO::getColumnId, columnId);
        return columnArticleMapper.selectCount(query).intValue();
    }
    @Override
    public Integer countColumnArticles(SearchColumnArticleParams params) {
        return columnArticleMapper.countColumnArticlesByColumnIdArticleName(params.getColumnId(),
                params.getArticleTitle()).intValue();
    }

    /**
     * 查询教程
     */
    @Override
    public List<ColumnInfoDO> listColumnsByParams(SearchColumnParams params, PageParam pageParam) {
        LambdaQueryWrapper<ColumnInfoDO> query = Wrappers.lambdaQuery();
        // 加上判空条件
        query.like(StringUtils.isNotBlank(params.getColumn()), ColumnInfoDO::getColumnName, params.getColumn());
        query.last(PageParam.getLimitSql(pageParam))
                .orderByAsc(ColumnInfoDO::getSection)
                .orderByDesc(ColumnInfoDO::getUpdateTime);
        return baseMapper.selectList(query);

    }



    /**
     * 查询教程总数
     */
    @Override
    public Integer countColumnsByParams(SearchColumnParams params) {
        LambdaQueryWrapper<ColumnInfoDO> query = Wrappers.lambdaQuery();
        lambdaQuery().like(StringUtils.isNotBlank(params.getColumn()), ColumnInfoDO::getColumnName, params.getColumn());
        return baseMapper.selectCount(query).intValue();
    }

    /**
     * 根据教程ID查询文章信息列表
     * @return
     */
    @Override
    public List<ColumnArticleDTO> listColumnArticlesDetail(SearchColumnArticleParams params,
                                                           PageParam pageParam) {
        return columnArticleMapper.listColumnArticlesByColumnIdArticleName(params.getColumnId(),
                params.getArticleTitle(),
                pageParam);
    }
}
