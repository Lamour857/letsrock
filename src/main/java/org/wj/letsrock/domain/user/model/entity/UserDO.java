package org.wj.letsrock.domain.user.model.entity;

import java.io.Serializable;
import java.util.Collection;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 用户登录表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@ApiModel(value="User对象", description="用户登录表")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class UserDO extends BaseDO implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "第三方用户ID")
    private String third_account_id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    @JsonIgnore
    private String password;


    @ApiModelProperty(value = "登录方式: 0-微信登录，1-账号密码登录")
    private Integer loginType;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;

    private String phone;

    private String role;
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return false;
    }
}
