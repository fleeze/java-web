package com.zz.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zz.response.HttpStatus;
import com.zz.response.ResponseObject;
import com.zz.security.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public TokenInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入拦截器");
        System.out.println(request.getRequestURI());
        System.out.println("Authorization " + request.getHeader("Authorization"));
        // 在这里进行 Token 验证的逻辑，获取 userId
        String token = request.getHeader("Authorization"); // 假设 Token 存放在请求头的 Authorization 字段中
        // 进行 Token 验证的逻辑，获取 userId
        System.out.println("请求token：" + token);
        int userId = JwtUtil.getUserId(token);
        if (userId != -1) {
            // 将 userId 放入请求属性，以便后续的处理方法可以获取
            request.setAttribute("userId", userId);
            return true;
        } else {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.getCode());
            response.getWriter().write(objectMapper.writeValueAsString(ResponseObject.error(HttpStatus.UNAUTHORIZED.getCode(), "请先登录")));
            return false;
        }
    }
}
