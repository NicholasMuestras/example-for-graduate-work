package ru.skypro.homework.controller;

import io.swagger.model.NewPassword;
import io.swagger.model.UpdateUser;
import io.swagger.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.service.UsersService;
import ru.skypro.homework.service.impl.CustomUserDetailsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)

public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersService usersService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    private User user;
    private UpdateUser updateUser;
    private NewPassword newPassword;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setFirstName("Ben");
        user.setLastName("Gun");
        user.setPhone("+79991234567");

        updateUser = new UpdateUser();
        updateUser.setFirstName("Ben");
        updateUser.setLastName("Gun");
        updateUser.setPhone("+79991234567");

        newPassword = new NewPassword();
        newPassword.setCurrentPassword("currentPassword");
        newPassword.setNewPassword("newPassword");
    }

    @Test
    @WithMockUser
    public void getUser_ShouldReturnUser_WhenUserExists() throws Exception {
        when(usersService.getUser()).thenReturn(user);

        mockMvc.perform(get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()));
    }

    @Test
    @WithMockUser
    public void setPassword_ShouldReturnOk_WhenPasswordUpdatedSuccessfully() throws Exception {
        when(usersService.setPassword(any(NewPassword.class))).thenReturn(true);

        mockMvc.perform(patch("/users/set_password")
                        .content("{\"currentPassword\":\"currentPassword\",\"newPassword\":\"newPassword\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void setPassword_ShouldReturnForbidden_WhenPasswordUpdateFailed() throws Exception {
        when(usersService.setPassword(any(NewPassword.class))).thenReturn(false);

        mockMvc.perform(patch("/users/set_password")
                        .content("{\"currentPassword\":\"currentPassword\",\"newPassword\":\"newPassword\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    public void updateUser_ShouldReturnUpdatedUser_WhenUpdateSuccessful() throws Exception {
        when(usersService.updateUser(any(UpdateUser.class))).thenReturn(updateUser);

        mockMvc.perform(patch("/users/me")
                        .content("{\"firstName\":\"Ben\",\"lastName\":\"Gun\",\"phone\":\"+79991234567\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(updateUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updateUser.getLastName()))
                .andExpect(jsonPath("$.phone").value(updateUser.getPhone()));
    }

    @Test
    @WithMockUser
    public void updateUserImage_ShouldReturnOk_WhenImageUpdatedSuccessfully() throws Exception {
        when(usersService.updateUserImage(any(byte[].class))).thenReturn(true);

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/users/me/image")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void updateUserImage_ShouldReturnInternalServerError_WhenImageUpdateFailed() throws Exception {
        when(usersService.updateUserImage(any(byte[].class))).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/users/me/image")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isInternalServerError());
    }
}
