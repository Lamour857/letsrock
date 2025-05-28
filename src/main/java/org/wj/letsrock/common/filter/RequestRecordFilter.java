package org.wj.letsrock.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.http.HttpMethod;
import org.wj.letsrock.utils.EnvironmentUtil;
import org.wj.letsrock.utils.IpUtil;
import org.wj.letsrock.utils.MdcUtil;
import org.wj.letsrock.utils.StopWatchUtil;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;

import javax.annotation.Resource;
import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Optional;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-12:37
 **/
@Slf4j
public class RequestRecordFilter implements Filter {

    private static final String GLOBAL_TRACE_ID_HEADER = "g-trace-id";



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest request = null;
        String uri=((HttpServletRequest) servletRequest).getRequestURI();
        StopWatchUtil stopWatch = new StopWatchUtil(uri+": 请求耗时");
        try {
            stopWatch.start(uri+": 请求参数构建1");
            request = this.initReqInfo((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
            stopWatch.stop();
            stopWatch.start("业务执行");
            filterChain.doFilter(request, servletResponse);
        } finally {
            if (stopWatch.isRunning()) {
                // 避免doFilter执行异常，导致上面的 stopWatch无法结束，这里先首当结束一下上次的计数
                stopWatch.stop();
            }
            stopWatch.start("输出请求日志");
            buildRequestLog(RequestInfoContext.getReqInfo(), request, System.currentTimeMillis() - start);
            // 一个链路请求完毕，清空MDC相关的变量(如GlobalTraceId，用户信息)
            MdcUtil.clear();
            RequestInfoContext.clear();
            stopWatch.stop();

            if (!isStaticURI(request) && !EnvironmentUtil.isPro()) {
                log.info("{} - cost:\n{}", request.getRequestURI(), stopWatch.prettyPrint());
            }
        }
    }

    private void buildRequestLog(RequestInfoContext.ReqInfo req, HttpServletRequest request, long costTime) {
        if (req == null || isStaticURI(request)) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append("method=").append(request.getMethod()).append("; ");
        if (StringUtils.isNotBlank(req.getReferer())) {
            msg.append("referer=").append(URLDecoder.decode(req.getReferer())).append("; ");
        }
        msg.append("remoteIp=").append(req.getClientIp());
        msg.append("; agent=").append(req.getUserAgent());

        if (req.getUserId() != null) {
            // 打印用户信息
            msg.append("; user=").append(req.getUserId());
        }

        msg.append("; uri=").append(request.getRequestURI());
        if (StringUtils.isNotBlank(request.getQueryString())) {
            msg.append('?').append(URLDecoder.decode(request.getQueryString()));
        }

        msg.append("; payload=").append(req.getPayload());
        msg.append("; cost=").append(costTime);
        log.info("{}", msg);

        // 保存请求计数
        //statisticsSettingService.saveRequestCount(req.getClientIp());
    }

    private HttpServletRequest initReqInfo(HttpServletRequest request, HttpServletResponse response) {
        if (isStaticURI(request)) {
            // 静态资源直接放行
            return request;
        }

        StopWatchUtil stopWatch = new StopWatchUtil(request.getRequestURI()+": 请求参数构建2");
        try {
            stopWatch.start("traceId");
            // 添加全链路的traceId
            MdcUtil.addTraceId();
            stopWatch.stop();

            stopWatch.start("请求基本信息");
            // 手动写入一个session，借助 OnlineUserCountListener 实现在线人数实时统计
            request.getSession().setAttribute("latestVisit", System.currentTimeMillis());

            RequestInfoContext.ReqInfo reqInfo = new RequestInfoContext.ReqInfo();
            reqInfo.setHost(request.getHeader("host"));
            reqInfo.setPath(request.getPathInfo());
            if (reqInfo.getPath() == null) {
                String url = request.getRequestURI();
                int index = url.indexOf("?");
                if (index > 0) {
                    url = url.substring(0, index);
                }
                reqInfo.setPath(url);
            }
            reqInfo.setReferer(request.getHeader("referer"));
            reqInfo.setClientIp(IpUtil.getClientIp(request));
            reqInfo.setUserAgent(request.getHeader("User-Agent"));
            
            //reqInfo.setDeviceId(getOrInitDeviceId(request, response));

            request = this.wrapperRequest(request, reqInfo);
            stopWatch.stop();

//            stopWatch.start("登录用户信息");
//            // 初始化登录信息, 用户身份识别
//            globalInitService.initLoginUser(reqInfo);
//            stopWatch.stop();

            RequestInfoContext.addReqInfo(reqInfo);
            stopWatch.start("回写traceId");
            // 返回头中记录traceId
            response.setHeader(GLOBAL_TRACE_ID_HEADER, Optional.ofNullable(MdcUtil.getTraceId()).orElse(""));
            stopWatch.stop();
        } catch (Exception e) {
            log.error("init reqInfo error!", e);
        } finally {
            if (!EnvironmentUtil.isPro()) {
                log.info("{} -> 请求构建耗时: \n{}", request.getRequestURI(), stopWatch.prettyPrint());
            }
        }

        return request;

    }

    private HttpServletRequest wrapperRequest(HttpServletRequest request, RequestInfoContext.ReqInfo reqInfo) {
        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            return request;
        }

        BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        reqInfo.setPayload(requestWrapper.getBodyString());
        return requestWrapper;
    }

    private boolean isStaticURI(HttpServletRequest request) {
        return request == null
                || request.getRequestURI().endsWith("css")
                || request.getRequestURI().endsWith("js")
                || request.getRequestURI().endsWith("png")
                || request.getRequestURI().endsWith("ico")
                || request.getRequestURI().endsWith("gif")
                || request.getRequestURI().endsWith("svg")
                || request.getRequestURI().endsWith("min.js.map")
                || request.getRequestURI().endsWith("min.css.map");
    }


}
