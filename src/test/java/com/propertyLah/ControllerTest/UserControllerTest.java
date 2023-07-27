package com.propertyLah.ControllerTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import com.propertyLah.controller.UserController;
import com.propertyLah.model.User;
import com.propertyLah.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @MockBean
    private BindingResult bindingResult;

    @Test
    @WithMockUser
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/user/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser
    public void testRegisterUser_WhenUserDoesNotExist() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(userService.findUserByEmail(Mockito.anyString())).thenReturn(null);

        mockMvc.perform(post("/user/registration")
                .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    @WithMockUser
    public void testRegisterUser_WhenUserExists() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(userService.findUserByEmail(Mockito.anyString())).thenReturn(user);

        mockMvc.perform(post("/user/registration")
                .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    @WithMockUser
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testShowProfilePage() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        
        Mockito.when(userService.findUserByEmail(Mockito.anyString())).thenReturn(user);

        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testChangePassword_Success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("oldPassword");

        Mockito.when(userService.findUserByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(bCryptPasswordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        mockMvc.perform(post("/user/changePassword")
                .param("currentPassword", "oldPassword")
                .param("newPassword", "newPassword")
                .param("confirmPassword", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("message", "Password changed successfully."));
    }
}

