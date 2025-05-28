package org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.wj.letsrock.domain.article.model.entity.ArticleDetailDO;

/**
 * <p>
 * 文章详情表 Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
public interface ArticleDetailMapper extends BaseMapper<ArticleDetailDO> {

    /**
     * 更新正文
     * fixme: 这里的版本迭代还没有管理起来；后续若存在审核中间态，则可以针对上线之后的文章，修改内容之后生成新的一条审核中的记录，版本 +1；而不是直接在原来的记录上进行版本+1
     *
     * @param articleId
     * @param content
     * @return
     */
    @Update("update article_detail set `content` = #{content}, `version` = `version` + 1 where article_id = #{articleId} and `deleted`=0 order by `version` desc limit 1")
    int updateContent(long articleId, String content);
}
