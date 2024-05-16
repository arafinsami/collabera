package com.collabera.controller;

import com.collabera.dto.AppUserDTO;
import com.collabera.entity.AppUser;
import com.collabera.mapper.UserMapper;
import com.collabera.service.AppUserService;
import com.collabera.validators.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.collabera.exception.ApiError.fieldError;
import static com.collabera.utils.ResponseBuilder.error;
import static com.collabera.utils.ResponseBuilder.success;
import static com.collabera.utils.StringUtils.toJson;
import static org.springframework.http.ResponseEntity.badRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "User API")
@RequestMapping(path = "user")
public class UserController {

    private final UserValidator validator;

    private final UserMapper mapper;

    private final AppUserService appUserService;

    @PostMapping("/register")
    @Operation(summary = "register a new user")
    public ResponseEntity<JSONObject> save(@Valid @RequestBody AppUserDTO request, BindingResult bindingResult) {
        ValidationUtils.invokeValidator(validator, request, bindingResult);

        if (bindingResult.hasErrors()) {
            return badRequest().body(error(fieldError(bindingResult)).getJson());
        }
        AppUser appUser = appUserService.save(request);
        log.info("register user response: {} ", toJson(appUser));
        AppUserDTO dto = mapper.from(appUser);
        return new ResponseEntity<>(success(dto).getJson(), HttpStatus.CREATED);
    }
}
