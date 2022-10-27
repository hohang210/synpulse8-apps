package com.oliver.accountBackend.aduit;

import com.oliver.apiGateway.domain.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * An aop class representing a stream writer for audit trail events.
 * <p>
 * For simplicity reasons, only log out a string message of audit event currently
 * Future implementations of Audit can be build to write to more appropriate systems
 *
 */
@Component
@Aspect
@Slf4j
public class AuditStream {
    @Pointcut("execution(* com.oliver.accountBackend.controller.*.*(..))")
    public void audit() {}

    @Before("audit()")
    public void beforeAdvice() {
        ServletRequestAttributes servletRequestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes == null) {
            log.info("No request for the audit event");
            return;
        }

        HttpServletRequest request = servletRequestAttributes.getRequest();
        String url = request.getRequestURL().toString();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        log.info(
                String.format(
                        "User - %d is trying to request resource - %s",
                        loginUser.getUser().getId(),
                        url
                )
        );
    }
}
