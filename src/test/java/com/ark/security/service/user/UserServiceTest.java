package com.ark.security.service.user;

import com.ark.security.exception.BadCredentialsException;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.models.user.User;
import com.ark.security.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("password");

        when(userRepository.existsUserByUsername(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSaveUserDuplicateUsername() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");

        when(userRepository.existsUserByUsername(anyString())).thenReturn(true);

        assertThrows(DuplicateException.class, () -> userService.saveUser(user));
    }

    @Test
    void testSaveUserDuplicateEmail() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");

        when(userRepository.existsUserByUsername(anyString())).thenReturn(false);
        when(userRepository.existsUserByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateException.class, () -> userService.saveUser(user));
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(1, user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1, user));
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1));
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("test");

        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("test");

        assertNotNull(result);
        assertEquals("test", result.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> userService.loadUserByUsername("test"));
    }



}