package org.wj.letsrock.infrastructure.security.filter;




import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.wj.letsrock.infrastructure.security.token.PasswordAuthenticationToken;
import org.wj.letsrock.infrastructure.security.token.VerifyCodeAuthenticationToken;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.exception.AuthException;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.utils.JsonUtil;
import org.wj.letsrock.domain.user.model.request.UserAuthReq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wujia
 * @description: 对来自/auth/**认证请求进行过滤
 * @createTime: 2024-12-20-13:50
 **/
@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String PASSWORD_REG = "^[A-Za-z0-9]{5,12}$";
    private static final String VERIFY_CODE_REG = "^\\d{6}$";
    private static final String PHONE_REG = "^1[3-9]\\d{9}$";

    public AuthenticationFilter() {
        super("/api/auth/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        log.info(RequestInfoContext.getReqInfo().toString());
        UserAuthReq userReq = JsonUtil.toObj(RequestInfoContext.getReqInfo().getPayload(), UserAuthReq.class);
        String requestURI=request.getRequestURI();
        AbstractAuthenticationToken authRequest;
        if(requestURI.equals("/api/auth/password")){
            String password=userReq.getCredential();
            if(StringUtils.isNotBlank(password)&& !password.matches(PASSWORD_REG)){
                throw ExceptionUtil.ofAuthException(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"密码仅字母+数字，且长度为6-12位");
            }
            authRequest= new PasswordAuthenticationToken(userReq.getPrinciple(), password);
        }else if(requestURI.equals("/api/auth/verifyCode")){
            String verifyCode=userReq.getCredential();
            if(StringUtils.isNotBlank(verifyCode)&& !verifyCode.matches(VERIFY_CODE_REG)){
                throw ExceptionUtil.ofAuthException(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"验证码格式错误");
            }
            authRequest= new VerifyCodeAuthenticationToken(userReq.getPrinciple(), verifyCode);
        }else{
            throw ExceptionUtil.ofAuthException(StatusEnum.ILLEGAL_ARGUMENTS_MIXED,"不支持的认证路由");
        }
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
    protected String obtainPhoneNumber(HttpServletRequest request) {
        return request.getParameter("phone")==null?"":request.getParameter("phone");
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter("password")==null?"":request.getParameter("password");
    }
    protected String obtainVerifyCode(HttpServletRequest request) {
        return request.getParameter("verifyCode")==null?"":request.getParameter("verifyCode");
    }
    protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
