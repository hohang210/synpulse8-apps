package com.oliver.apiGateway.handler;

import com.alibaba.fastjson.JSON;
import com.oliver.response.ResponseResult;
import com.oliver.response.StatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class Synpulse8AccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        ResponseResult<String> responseData =
                new ResponseResult<>(StatusCode.ACCESS_ERROR, "Unauthorized");

        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(JSON.toJSONString(responseData));
    }
}
