package org.wj.letsrock.domain.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:55
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayCodeDTO implements Serializable {
    private static final long serialVersionUID = -2601714252107169062L;

    /**
     * base64格式的收款二维码图片
     */
    private String qrCode;

    /**
     * 内容
     */
    private String qrMsg;
}