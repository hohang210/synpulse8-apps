package com.oliver.apiGateway.handler;

import com.alibaba.fastjson.JSON;
import com.oliver.response.ResponseResult;
import com.oliver.response.StatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class Synpulse8AuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authenticationException
    ) throws IOException {
        ResponseResult<String> responseData =
                new ResponseResult<>(
                        StatusCode.LOGIN_ERROR,
                        authenticationException.getMessage());

        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(JSON.toJSONString(responseData));
    }
}
