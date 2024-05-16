package com.collabera.controller;

import com.collabera.model.Login;
import com.collabera.security.TokenProvider;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Login API")
@RequestMapping(path = "login")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider jwtProvider;

    private final UserDetailsService userDetailsService;

    @PostMapping
    @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Login.class))
    })
    public ResponseEntity<?> authenticationToken(@Valid @RequestBody Login login) throws AuthenticationException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getUsername(),
                        login.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(login.getUsername());
        final String accessToken = jwtProvider.generateToken(userDetails);
        final String refreshToken = jwtProvider.generateRefreshToken(userDetails);
        Map<String, Object> token = new HashMap<>();
        token.put("token", accessToken);
        token.put("refreshToken", refreshToken);
        return ResponseEntity.ok(token);
    }
}
