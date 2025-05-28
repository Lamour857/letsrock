package org.wj.letsrock.domain.article.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.repository.ColumnRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ColumnRepositoryImpl;
import org.wj.letsrock.domain.article.service.ColumnService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:50
 **/
@Service
public class ColumnServiceImpl implements ColumnService {
    @Autowired
    private ColumnRepository columnDao;
    @Override
    public Long getTutorialCount() {
        return columnDao.countColumnArticles();
    }
}
