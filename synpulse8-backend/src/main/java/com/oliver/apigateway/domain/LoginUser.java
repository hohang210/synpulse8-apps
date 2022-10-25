package com.oliver.apigateway.domain;

import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginUser implements UserDetails {
    private User user;

    List<String> permissions = new ArrayList<>();

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    /**
     * Create a login user to store in redis for authorization.
     * Will obtain a list of authority from user.
     *
     * @param user A logged-in user.
     */
    public LoginUser(User user) {
        this.user = user;

        List<Role> roles = user.showAllRoles();
        for (Role role : roles) {
            role.showAllSystemMenus().forEach(
                    systemMenu -> {
                        if (systemMenu.getPermission() == SystemMenu.Permission.GRANT) {
                            String resource = systemMenu.getResource();
                            permissions.add(resource);
                            grantedAuthorities.add(
                                    new SimpleGrantedAuthority(resource)
                            );
                        }
                    }
            );
        }
    }

    public User getUser() {
        return user;
    }

    public List<String> getPermissions() {
        return permissions;
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
