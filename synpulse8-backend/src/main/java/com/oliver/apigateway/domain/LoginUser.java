package com.oliver.apiGateway.domain;

import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginUser implements UserDetails {
    private static final long serialVersionUID = -3233980433721731028L;
    private User user;

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    /**
     * Create a login user to store in redis for authorization.
     * Will obtain a list of authority from user.
     *
     * @param user A logged-in user.
     */
    public LoginUser(User user, List<SystemMenu> systemMenus) {
        this.user = user;

        systemMenus.forEach(systemMenu -> 
                grantedAuthorities.add(
                        new SimpleGrantedAuthority(systemMenu.getResource())
                )
        );
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
