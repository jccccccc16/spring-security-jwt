package com.cjc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/13
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable, UserDetails {
    private Integer id;
    private String username;
    private String password;
    

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public boolean isAccountNonExpired() {
        return false;
    }

    public boolean isAccountNonLocked() {
        return false;
    }

    public boolean isCredentialsNonExpired() {
        return false;
    }

    public boolean isEnabled() {
        return false;
    }
}
