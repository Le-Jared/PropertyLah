package com.propertyLah.ServiceTest;

import com.propertyLah.model.User;
import com.propertyLah.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.propertyLah.service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        Mockito.when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.saveUser(user);

        assertNotNull(user.getPassword());
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void testFindUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        User found = userService.findUserByEmail("test@example.com");

        assertNotNull(found);
        assertEquals(user.getEmail(), found.getEmail());
    }
}

