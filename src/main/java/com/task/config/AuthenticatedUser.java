package com.task.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthenticatedUser extends User {

    private com.task.entity.User user;

    public AuthenticatedUser(com.task.entity.User user) {
        super(user.getUsername(), user.getPassword(), true, true, true, true, getAuthorities(user));
        this.user = user;
    }

    public com.task.entity.User getUser() {
        return user;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(com.task.entity.User user) {
        Set<String> roleAndPermissions = new HashSet<>();
        roleAndPermissions.add(user.getRole());
        String[] roleNames = new String[roleAndPermissions.size()];
        Collection<GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(roleAndPermissions.toArray(roleNames));
        return authorities;
    }
}
