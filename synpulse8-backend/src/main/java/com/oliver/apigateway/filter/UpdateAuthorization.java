package com.oliver.apiGateway.filter;

import com.oliver.apiGateway.domain.LoginUser;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.manager.UserManager;
import com.oliver.util.redis.RedisCache;
import com.oliver.util.redis.RedisKeyCreator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.oliver.configuration.constant.JWT_EXPIRATION_TIME;

@Aspect
@Component
@Slf4j
public class UpdateAuthorization {
    private UserManager userManager;

    private RedisCache redisCache;

    @Pointcut("execution(* com.oliver.accountBackend.controller.AccountController.createAccount(..))")
    public void updateAuthorization() {}

    @AfterReturning("updateAuthorization()")
    public void afterReAdvice() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();

        log.info(
                String.format(
                        "Updating user - %d in redis, " +
                                "since logged-in user grants a new resource.",
                        user.getId()
                )
        );
        List<SystemMenu> systemMenus =
                userManager.showAllGrantedResources(user.getId());
        LoginUser newLoginUser = new LoginUser(user, systemMenus);
        redisCache.saveObject(
                RedisKeyCreator.createLoginUserKey(user.getId().toString()),
                newLoginUser,
                Integer.valueOf(JWT_EXPIRATION_TIME),
                TimeUnit.MILLISECONDS
        );
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }
}
