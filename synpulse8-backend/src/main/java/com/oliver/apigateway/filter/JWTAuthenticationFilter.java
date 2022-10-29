package com.oliver.apiGateway.filter;

import com.oliver.apiGateway.domain.LoginUser;
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

        jwt = jwt.replace("Bearer ", "");

        // Parse token and retrieve logged-in user from redis
        String userId;
        LoginUser loginUser;
        String validJwt;
        try {
            Claims claims = JWTUtil.parseJWT(jwt);
            userId = claims.getSubject();
            loginUser = redisCache.getObject(
                    RedisKeyCreator.createLoginUserKey(userId)
            );
            validJwt = redisCache.getObject(
                    RedisKeyCreator.createLoginUserJWTKey(userId)
            );
        } catch (Exception e) {
            log.debug("Invalid JWT - {}", e.getMessage());
            throw new RuntimeException("Invalid JWT");
        }

        if (!validJwt.equals(jwt)) {
            log.info("User - {} 's jwt {} is invalid", userId, jwt);
            throw new RuntimeException(
                    String.format(
                            "User - %s 's JWT is invalid",
                            userId
                    )

            );
        }

        if (loginUser == null) {
            log.info(
                    "User - {} with jwt - {} is not logged-in",
                    userId,
                    jwt
            );
            throw new RuntimeException(
                    String.format(
                            "User - %s is not logged-in",
                            userId
                    )
            );
        }

        // Authentication
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
