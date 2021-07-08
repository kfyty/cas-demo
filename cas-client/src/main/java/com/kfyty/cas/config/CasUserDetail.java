package com.kfyty.cas.config;

import com.kfyty.cas.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/7/7 18:49
 * @email kfyty725@hotmail.com
 */
@Getter
public class CasUserDetail implements UserDetails {
    private final User user;
    private final Set<String> permissions;
    private final Set<SimpleGrantedAuthority> authorities;

    public CasUserDetail(User user) {
        Objects.requireNonNull(user);
        this.user = user;
        this.permissions = user.getUrl() == null ? Collections.emptySet() : Arrays.stream(user.getUrl().split(",")).collect(Collectors.toSet());
        this.authorities = user.getRole() == null ? Collections.emptySet() : Arrays.stream(user.getRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        this.user.setCredentialsExpired(false);
    }

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
        return !this.user.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
