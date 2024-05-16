package com.collabera.model;

import com.collabera.entity.AppUser;

public final class AuthUserFactory {

    private AuthUserFactory() {
    }

    public static AuthUser create(AppUser user) {
        return new AuthUser(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getEnabled());
    }
}
