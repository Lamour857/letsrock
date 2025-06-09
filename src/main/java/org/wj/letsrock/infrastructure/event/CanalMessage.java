package org.wj.letsrock.infrastructure.event;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-08-17:41
 **/
@Data
@ToString
public class CanalMessage {
    private List<Map<String, Object>> data;      // 变更的数据行
    private String database;                    // 数据库名
    private Long es;                            // 事件时间戳(millis)
    private String gtid;                        // GTID（全局事务ID）
    private Long id;                         // 消息ID
    private Boolean isDdl;                      // 是否为DDL语句
    private Map<String, String> mysqlType;       // MySQL字段类型映射
    private List<Map<String, Object>> old;      // 更新前的旧值（仅UPDATE有效）
    private List<String> pkNames;               // 主键字段名
    private String sql;                         // 原始SQL（DDL语句）
    private Map<String, Integer> sqlType;        // JDBC类型映射
    private String table;                       // 表名
    private Long ts;                            // 消息处理时间戳
    private String type;
}
