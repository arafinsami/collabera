package com.collabera.validators;

import com.collabera.dto.AppUserDTO;
import com.collabera.entity.AppUser;
import com.collabera.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

import static com.collabera.utils.Constants.ALREADY_EXIST;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final AppUserService appUserService;

    @Override
    public boolean supports(Class<?> clazz) {
        return AppUserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AppUserDTO dto = (AppUserDTO) target;
        Optional<AppUser> user = appUserService.findByEmail(dto.getEmail());
        if (user.isPresent()) {
            errors.rejectValue("email", "", ALREADY_EXIST);
        }
    }
}
