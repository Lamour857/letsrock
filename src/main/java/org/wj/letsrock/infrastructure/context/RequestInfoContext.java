package org.wj.letsrock.infrastructure.context;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.user.model.entity.UserDO;

import java.security.Principal;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-12:59
 **/
@Component
public class RequestInfoContext {
    private static ThreadLocal<ReqInfo> contexts = new ThreadLocal<>();

    public static void addReqInfo(ReqInfo reqInfo) {
        contexts.set(reqInfo);
    }

    public static void clear() {
        contexts.remove();
    }

    public static ReqInfo getReqInfo() {
        return contexts.get();
    }

    @Data
    @ToString
    public static class ReqInfo implements Principal {
        /**
         * appKey
         */
        private String appKey;
        /**
         * 访问的域名
         */
        private String host;
        /**
         * 访问路径
         */
        private String path;
        /**
         * 客户端ip
         */
        private String clientIp;
        /**
         * referer
         */
        private String referer;
        /**
         * post 表单参数
         */
        private String payload;
        /**
         * 设备信息
         */
        private String userAgent;

        /**
         * 登录的会话
         */
        private String session;

        /**
         * 用户id
         */
        private Long userId;
        /**
         * 用户信息
         */
        private UserDO user;
        /**
         * 消息数量
         */
        private Integer msgNum;

        //private Seo seo;

        private String deviceId;

        @Override
        public String getName() {
            return session;
        }
    }
}
