package com.oliver.apigateway.filter;

import com.oliver.apigateway.domain.LoginUser;
import com.oliver.tenancy.domain.User;
import com.oliver.util.JWTUtil;
import com.oliver.util.redis.RedisCache;
import com.oliver.util.redis.RedisKeyCreator;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authenticate requests with header 'Authorization: Bearer jwt-token'.
 */
@Component
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Grab JWT token
        String jwt = request.getHeader("Authorization");
        if (!StringUtils.hasText(jwt)) {
            // User does not logged-in
            // Do the followed filter
            filterChain.doFilter(request, response);
            return;
        }

        jwt = jwt.replace("bearer ", "");

        // Parse token and retrieve logged-in user from redis
        String userId = null;
        User user;
        try {
            Claims claims = JWTUtil.parseJWT(jwt);
            userId = claims.getSubject();
            user = redisCache.getObject(
                    RedisKeyCreator.createLoginUserKey(userId)
            );
        } catch (Exception e) {
            log.error("Invalid JWT for user - %s");
            log.error(e.getMessage());
            throw new RuntimeException(
                    "Invalid JWT for user - %s"
            );
        }

        if (user == null) {
            log.error("User - %s is not logged-in");
            throw new RuntimeException(
                    "User - %s is not logged-in"
            );
        }

        // Authentication
        LoginUser loginUser = new LoginUser(user);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginUser, null, loginUser.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }
}
