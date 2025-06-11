package org.wj.letsrock.domain.user.model.dto;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-10-13:57
 **/
@Data
public class LoginResponseDTO {
    private String token;
    private Long userId;
}
