package com.collabera.controller;

import com.collabera.entity.AppUser;
import com.collabera.repository.AppUserRepository;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

@RestController
@RequiredArgsConstructor
@Tag(name = "Setup api")
@RequestMapping(path = "setup")
public class SetupController {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = AppUser.class))
    })
    public ResponseEntity<?> setup() {
        AppUser user = new AppUser();
        user.setFullName("MD. SAMIUL ARAFIN");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setEnabled(true);
        user.setLastPasswordResetDate(Calendar.getInstance().getTime());
        appUserRepository.save(user);
        return ResponseEntity.ok("DONE");
    }
}