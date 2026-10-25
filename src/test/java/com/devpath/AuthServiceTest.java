package com.devpath;

import com.devpath.dto.AuthRequest;
import com.devpath.dto.AuthResponse;
import com.devpath.dto.RegisterRequest;
import com.devpath.entity.User;
import com.devpath.entity.enums.Role;
import com.devpath.exception.BadRequestException;
import com.devpath.repository.UserRepository;
import com.devpath.security.JwtUtils;
import com.devpath.security.UserDetailsImpl;
import com.devpath.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(
                "Jane Doe",
                "jane@example.com",
                "encodedPassword",
                Role.ROLE_STUDENT,
                "Backend Engineer",
                "Engineering College"
        );
        sampleUser.setId(10L);
    }

    @Test
    @DisplayName("Should successfully register a new student user")
    void shouldRegisterNewStudent() {
        RegisterRequest req = new RegisterRequest("Jane Doe", "jane@example.com", "Password@123", "Backend Engineer", "Engineering College");

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Password@123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserDetailsImpl userDetails = UserDetailsImpl.build(sampleUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn("mock-jwt-token-123");

        AuthResponse response = authService.register(req);

        assertNotNull(response);
        assertEquals("jane@example.com", response.getEmail());
        assertEquals("mock-jwt-token-123", response.getToken());
        assertEquals("ROLE_STUDENT", response.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should reject registration when email already exists")
    void shouldRejectDuplicateEmail() {
        RegisterRequest req = new RegisterRequest("Jane Doe", "jane@example.com", "Password@123", "Backend Engineer", "Engineering College");
        when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(req));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully authenticate and login user")
    void shouldLoginSuccessfully() {
        AuthRequest req = new AuthRequest("jane@example.com", "Password@123");
        UserDetailsImpl userDetails = UserDetailsImpl.build(sampleUser);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn("mock-jwt-login-token");
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(sampleUser));

        AuthResponse response = authService.login(req);

        assertNotNull(response);
        assertEquals("mock-jwt-login-token", response.getToken());
        assertEquals("Jane Doe", response.getName());
    }
}
