package com.oliver.apigateway.configuration;

import com.oliver.apigateway.handler.Synpulse8AccessDeniedHandler;
import com.oliver.apigateway.handler.Synpulse8AuthenticationEntryPoint;
import com.oliver.apigateway.filter.JWTAuthenticationFilter;
import com.oliver.apigateway.filter.JWTUsernamePasswordAuthenticationFilter;
import com.oliver.apigateway.handler.Synpulse8LogoutSuccessHandler;
import com.oliver.util.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfiguration {
    private AuthenticationConfiguration authenticationConfiguration;

    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private RedisCache redisCache;

    private Synpulse8LogoutSuccessHandler logoutSuccessHandler;

    private Synpulse8AuthenticationEntryPoint authenticationEntryPoint;

    private Synpulse8AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .permitAll()
                .and()
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        LogoutFilter.class
                )
                .addFilterAfter(
                        new JWTUsernamePasswordAuthenticationFilter(
                                authenticationManager(),
                                redisCache
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                    .antMatchers("/signUp").anonymous()
                    .antMatchers("/login").anonymous()
                    .antMatchers("/account/**").authenticated()
                    .antMatchers("/logout").authenticated()
                    .anyRequest().authenticated();


        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    public void setAuthenticationConfiguration(
            AuthenticationConfiguration authenticationConfiguration
    ) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Autowired
    public void setJwtAuthenticationFilter(JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Autowired
    public void setLogoutSuccessHandler(Synpulse8LogoutSuccessHandler logoutSuccessHandler) {
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Autowired
    public void setAuthenticationEntryPoint(Synpulse8AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Autowired
    public void setAccessDeniedHandler(Synpulse8AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }
}
