package com.collabera.mapper;

import com.collabera.dto.AppUserDTO;
import com.collabera.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AppUserDTO from(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public AppUser save(AppUserDTO request) {
        AppUser appUser = new AppUser();
        appUser.setUsername(request.getUsername());
        appUser.setEmail(request.getEmail());
        appUser.setPassword(request.getPassword());
        return appUser;
    }
}
