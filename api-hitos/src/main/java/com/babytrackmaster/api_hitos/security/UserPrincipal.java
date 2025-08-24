package com.babytrackmaster.api_hitos.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;

    public UserPrincipal(Long id,
                         String username,
                         String password,
                         Collection<? extends GrantedAuthority> authorities,
                         boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    // Usado por tu helper mediante reflexión
    public Long getId() {
        return id;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    public String getPassword() { return password; }
    public String getUsername() { return username; }
    public boolean isAccountNonExpired() { return true; }
    public boolean isAccountNonLocked() { return true; }
    public boolean isCredentialsNonExpired() { return true; }
    public boolean isEnabled() { return enabled; }
}
