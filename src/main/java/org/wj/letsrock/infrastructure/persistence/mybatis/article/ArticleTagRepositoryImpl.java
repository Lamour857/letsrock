package org.wj.letsrock.infrastructure.persistence.mybatis.article;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.article.repository.ArticleTagRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.ArticleTagMapper;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleTagDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-19:06
 **/
@Repository
public class ArticleTagRepositoryImpl extends ServiceImpl<ArticleTagMapper, ArticleTagDO> implements ArticleTagRepository {
    @Override
    public List<TagDTO> queryArticleTagDetails(Long articleId) {
        return baseMapper.listArticleTagDetails(articleId);
    }
    @Override
    public List<ArticleTagDO> listArticleTags(@Param("articleId") Long articleId) {
        return lambdaQuery().eq(ArticleTagDO::getArticleId, articleId).eq(ArticleTagDO::getDeleted, YesOrNoEnum.NO.getCode()).list();
    }

    @Transactional
    @Override
    public void batchSave(Long articleId, Set<Long> tags) {
        List<ArticleTagDO> insertList = new ArrayList<>(tags.size());
        tags.forEach(s -> {
            ArticleTagDO tag = new ArticleTagDO();
            tag.setTagId(s);
            tag.setArticleId(articleId);
            tag.setDeleted(YesOrNoEnum.NO.getCode());
            insertList.add(tag);
        });
        saveBatch(insertList);
    }

    /**
     * 更新文章标签
     * 1. 原来有，新的没有；则删除旧的
     * 2. 原来有，新的改变，则替换旧的
     * 3. 原来没有，新的有，则插入
     *
     * @param articleId
     * @param newTags
     */
    @Override
    public void updateTags(Long articleId, Set<Long> newTags) {
        List<ArticleTagDO> dbTags = listArticleTags(articleId);
        // 在旧的里面，不在新的里面的标签，设置为删除
        List<Long> toDeleted = new ArrayList<>();
        dbTags.forEach(tag -> {
            if (!newTags.contains(tag.getTagId())) {
                toDeleted.add(tag.getId());
            } else {
                // 移除已经存在的记录
                newTags.remove(tag.getTagId());
            }
        });
        if (!toDeleted.isEmpty()) {
            baseMapper.deleteBatchIds(toDeleted);
        }

        if (!newTags.isEmpty()) {
            batchSave(articleId, newTags);
        }
    }
}
