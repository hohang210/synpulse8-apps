package com.oliver.apiGateway.handler;

import com.alibaba.fastjson.JSON;
import com.oliver.apiGateway.domain.LoginUser;
import com.oliver.tenancy.domain.User;
import com.oliver.util.StatusCode;
import com.oliver.util.redis.RedisCache;
import com.oliver.util.ResponseResult;
import com.oliver.util.redis.RedisKeyCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class Synpulse8LogoutSuccessHandler implements LogoutSuccessHandler {
    private RedisCache redisCache;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        if (authentication == null) {
            log.warn("An unauthenticated user tried to logout.");
            response.sendError(
                    StatusCode.LOGIN_ERROR,
                    "User is not authenticated."
            );
            return;
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();
        String userId = user.getId().toString();

        log.info(String.format("User - %s tried to logout", userId));
        redisCache.deleteObject(RedisKeyCreator.createLoginUserKey(userId));
        log.info(String.format("User - %s logout successfully", userId));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response
                .getWriter()
                .print(
                        JSON.toJSON(
                                new ResponseResult<>(
                                        StatusCode.OK,
                                        "Log-out successfully"
                                )
                        )
                );
    }

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }
}
