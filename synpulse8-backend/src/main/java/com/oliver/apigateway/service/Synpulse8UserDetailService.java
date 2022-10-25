package com.oliver.apigateway.service;

import com.oliver.apigateway.domain.LoginUser;
import com.oliver.tenancy.UserManager;
import com.oliver.tenancy.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Synpulse8UserDetailService implements UserDetailsService {
    private UserManager userManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userManager.getUserByUsername(username);

        if (user == null) {
            log.error(String.format("User - %s cannot be found.", username));
            throw new UsernameNotFoundException(
                    String.format("User - %s cannot be found.", username)
            );
        }

        return new LoginUser(user);
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
