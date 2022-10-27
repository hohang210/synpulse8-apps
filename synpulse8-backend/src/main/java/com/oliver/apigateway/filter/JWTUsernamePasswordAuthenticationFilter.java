package com.oliver.apiGateway.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oliver.apiGateway.domain.LoginUser;
import com.oliver.apiGateway.domain.UserForm;
import com.oliver.tenancy.domain.User;
import com.oliver.util.JWTUtil;
import com.oliver.util.StatusCode;
import com.oliver.util.redis.RedisCache;
import com.oliver.util.ResponseResult;
import com.oliver.util.redis.RedisKeyCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.oliver.configuration.constant.JWT_EXPIRATION_TIME;

/**
 * Attempts to log in a user.
 * Saves user's info and jwt to redis if log-in successfully,
 * otherwise will throw `ValidationException`.
 */
@Slf4j
public class JWTUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserForm userForm = null;

    private AuthenticationManager authenticationManager;

    private RedisCache redisCache;

    public JWTUsernamePasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            RedisCache redisCache
    ) {
        // UsernamePasswordAuthenticationFilter listens to "/login" path.
        this.authenticationManager = authenticationManager;
        this.redisCache = redisCache;
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/login", "POST")
        );
    }

    /**
     * Try to compare between the submitted user's password
     * and the password stored in db.
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        UserForm userForm1;
        try {
            // Read submitted user form from request.
            userForm = new ObjectMapper().readValue(
                    request.getInputStream(),
                    UserForm.class
            );
        } catch (IOException e) {
            log.error("System error when try to read user credentials");
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        log.info(
                String.format(
                        "User - %s is attempting to log in.",
                        userForm.getUsername()
                )
        );

        // Create auth object (contains credentials) which will be used by auth manager
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userForm.getUsername(),
                        userForm.getPassword()
                );

        // Authentication manager authenticate the user,
        // and use Synpulse8UserDetailService::loadUserByUsername() method to load the user.
        return authenticationManager
                .authenticate(usernamePasswordAuthenticationToken);
    }

    /**
     * Saves user's info and jwt to redis if log-in successfully,
     * otherwise will throw `ValidationException`.
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        // Generates JWT for logged-in user.
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        User user = loginUser.getUser();
        String userId = user.getId().toString();
        String username = user.getUsername();
        String jwt = JWTUtil.createJWT(userId);

        // Saves logged-in user info and jwt to redis
        log.debug(String.format("Saving %s user's info to redis", username));
        redisCache.saveObject(
                RedisKeyCreator.createLoginUserKey(userId),
                loginUser,
                Integer.valueOf(JWT_EXPIRATION_TIME),
                TimeUnit.MILLISECONDS
        );

        log.info(String.format("User - %s is logged-in.", username));

        // Sending response back to frontend
        Map<String, String> responseData = new HashMap<>();
        responseData.put("jwt", jwt);
        ResponseResult<Map<String, String>> responseResult =
                new ResponseResult<>(StatusCode.OK, "Log-in successfully", responseData);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(JSON.toJSON(responseResult));
    }
}
