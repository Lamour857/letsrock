package org.wj.letsrock.infrastructure.persistence.es.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@Document(indexName = "user")
@Accessors
public class UserDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String username;


    @Field(type = FieldType.Integer)
    private Integer gender;

    @Field(type = FieldType.Integer)
    private Integer status;

    @Field(type = FieldType.Long)
    private Long articleCount;

    @Field(type = FieldType.Long)
    private Long followCount;

    @Field(type = FieldType.Long)
    private Long fansCount;

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Date)
    private Date updateTime;

    @Field(type = FieldType.Boolean)
    private Boolean isOfficial;
}
