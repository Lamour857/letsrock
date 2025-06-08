package org.wj.letsrock.infrastructure.persistence.es.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-06-21:01
 **/
@Data
@Document(indexName = "article")
public class ArticleDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Long)
    private Long authorId;

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Keyword)
    private String status;
}
