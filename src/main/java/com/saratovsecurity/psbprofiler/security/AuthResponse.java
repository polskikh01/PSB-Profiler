package com.saratovsecurity.psbprofiler.security;

import com.saratovsecurity.psbprofiler.entity.Role;
import com.saratovsecurity.psbprofiler.entity.UserEntity;
import lombok.Getter;

@Getter
public class AuthResponse {
    private Long id;
    private String login;
    private Role role;

    public AuthResponse(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.login = userEntity.getLogin();
        this.role = userEntity.getRole();
    }
}
