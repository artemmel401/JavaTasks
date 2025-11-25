package ru.artem.NauJava.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.artem.NauJava.dao.UserRepositoryImpl;
import ru.artem.NauJava.entity.User;
import ru.artem.NauJava.model.Role;
import ru.artem.NauJava.repository.UserRepository;
import ru.artem.NauJava.service.UserService;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepositoryImpl userRepositoryCustom;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepositoryCustom, userRepository, passwordEncoder);

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("rawPassword");
        testUser.setRole(Role.ADMIN);
    }


    @Test
    void addUser_ShouldEncodePasswordAndSetDefaultValues() {
        // Arrange
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.addUser(testUser);

        // Assert
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(testUser);

        assertTrue(testUser.getIsActive());
        assertEquals(Role.ADMIN, testUser.getRole());
        assertEquals(encodedPassword, testUser.getPassword());
    }

    @Test
    void addUser_ShouldHandleEmptyPassword() {
        // Arrange
        testUser.setPassword("");
        String encodedPassword = "encodedEmptyPassword";
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.addUser(testUser);

        // Assert
        verify(passwordEncoder).encode("");
        verify(userRepository).save(testUser);
        assertEquals(encodedPassword, testUser.getPassword());
    }

    // Тесты для метода loadUserByUsername

    @Test
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        // Arrange
        String username = "test@example.com";
        when(userRepositoryCustom.findByEmail(username)).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(testUser.getEmail(), userDetails.getUsername());
        assertEquals(testUser.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepositoryCustom).findByEmail(username);
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        String username = "nonexistent@example.com";
        when(userRepositoryCustom.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(username)
        );

        assertEquals("user not found", exception.getMessage());
        verify(userRepositoryCustom).findByEmail(username);
    }

    @Test
    void loadUserByUsername_WhenEmailIsNull_ShouldThrowException() {
        // Arrange
        when(userRepositoryCustom.findByEmail(null)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(null)
        );

        assertEquals("user not found", exception.getMessage());
        verify(userRepositoryCustom).findByEmail(null);
    }

    @Test
    void loadUserByUsername_WhenEmailIsEmpty_ShouldThrowException() {
        // Arrange
        when(userRepositoryCustom.findByEmail("")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("")
        );

        assertEquals("user not found", exception.getMessage());
        verify(userRepositoryCustom).findByEmail("");
    }
}
