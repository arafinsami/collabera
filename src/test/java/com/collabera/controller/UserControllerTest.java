package com.collabera.controller;

import com.collabera.dto.AppUserDTO;
import com.collabera.entity.AppUser;
import com.collabera.mapper.UserMapper;
import com.collabera.service.AppUserService;
import com.collabera.validators.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserMapper userMapper;

    @Mock
    private AppUserService userService;

    @Mock
    private UserValidator validator;

    @InjectMocks
    private UserController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .setValidator(validator)
                .build();
    }

    @Test
    public void testSave_Success() throws Exception {
        when(validator.supports(AppUserDTO.class)).thenReturn(true);
        BindingResult bindingResult = mock(BindingResult.class);
        lenient().when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.save(any(AppUserDTO.class))).thenReturn(new AppUser());
        lenient().when(userMapper.from(any(AppUser.class))).thenReturn(new AppUserDTO());
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new AppUserDTO())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andReturn();
        verify(userService, times(1)).save(any(AppUserDTO.class));
    }
}
