package org.wj.letsrock.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.wj.letsrock.model.vo.ResultVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-13:31
 **/
public class JsonUtil {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    public static <T> String toStr(T t) {
        try {
            return jsonMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> T toObj(String str, Class<T> clz) {
        try {
            return jsonMapper.readValue(str, clz);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static void writeResultVoToResponse(HttpServletResponse response, ResultVo<?> resVo) throws IOException {
        // 设置响应内容类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 将 ResVo 对象转换为 JSON 字符串并写入响应体
        jsonMapper.writeValue(response.getWriter(), resVo);
    }
}
