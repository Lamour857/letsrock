package org.wj.letsrock.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-16:09
 **/
@Data
public class BaseDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private Date updateTime;
}
