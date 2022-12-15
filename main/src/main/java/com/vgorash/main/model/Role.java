package com.vgorash.main.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class Role implements GrantedAuthority {

    enum RoleEnum{
        ROLE_USER, ROLE_DEVELOPER
    }

    private final RoleEnum roleEnum;

    public Role(RoleEnum roleEnum){
        this.roleEnum = roleEnum;
    }

    @Override
    public String getAuthority() {
        return roleEnum.toString();
    }
}
