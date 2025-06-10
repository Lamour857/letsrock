package org.wj.letsrock.infrastructure.persistence.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.wj.letsrock.infrastructure.persistence.es.model.UserDocument;

public interface UserEsRepository extends ElasticsearchRepository<UserDocument, Long> {
    // 可以添加自定义查询方法
}
