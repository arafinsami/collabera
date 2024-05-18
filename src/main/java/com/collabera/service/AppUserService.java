package com.collabera.service;

import com.collabera.dto.AppUserDTO;
import com.collabera.entity.AppUser;
import com.collabera.mapper.UserMapper;
import com.collabera.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    private final UserMapper mapper;

    @Transactional
    public AppUser save(AppUserDTO request) {
        AppUser appUser = mapper.save(request);
        appUserRepository.save(appUser);
        return appUser;
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }
}
