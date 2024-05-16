package com.collabera.service;

import com.collabera.dto.AppUserDTO;
import com.collabera.entity.AppUser;
import com.collabera.mapper.UserMapper;
import com.collabera.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void save_UserSavedSuccessfully() {
        AppUserDTO request = getAppUserDTO();
        AppUser mappedUser = new AppUser();
        mappedUser.setUsername(request.getUsername());
        mappedUser.setEmail(request.getEmail());
        when(userMapper.save(request)).thenReturn(mappedUser);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(mappedUser);
        AppUser savedUser = appUserService.save(request);
        assertEquals(mappedUser, savedUser);
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(appUserRepository, times(1)).save(mappedUser);
    }

    @Test
    void findByEmail_UserFoundSuccessfully() {
        String email = "myemail@gmail.com";
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        appUser.setEmail(email);
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(appUser));
        Optional<AppUser> foundUser = appUserService.findByEmail(email);
        assertEquals(Optional.of(appUser), foundUser);
    }

    @Test
    void findByEmail_UserNotFound() {
        String email = "myemail@gmail.com";
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<AppUser> foundUser = appUserService.findByEmail(email);
        assertEquals(Optional.empty(), foundUser);
    }

    private static AppUserDTO getAppUserDTO() {
        AppUserDTO request = new AppUserDTO();
        request.setUsername("testUser");
        request.setEmail("myemail@gmail.com");
        request.setPassword("password");
        return request;
    }
}
