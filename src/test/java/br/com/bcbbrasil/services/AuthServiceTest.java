package br.com.bcbbrasil.services;

import br.com.bcbbrasil.dto.AuthenticationDTO;
import br.com.bcbbrasil.dto.LoginResponseDTO;
import br.com.bcbbrasil.dto.RegisterDTO;
import br.com.bcbbrasil.models.User;
import br.com.bcbbrasil.models.UserRole;
import br.com.bcbbrasil.repository.UserRepository;
import br.com.bcbbrasil.service.AuthService;
import br.com.bcbbrasil.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService();
        authService.userRepository = userRepository;
        authService.jwtService = jwtService;
    }

    @Test
    void testLogin_Success() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String encryptedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        user.setRole(UserRole.USER);

        AuthenticationDTO authData = new AuthenticationDTO(email, password);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("token");

        LoginResponseDTO response = authService.login(authData);

        assertNotNull(response);
        assertEquals("token", response.token());
        assertEquals(UserRole.USER, response.role());
    }

    @Test
    void testLogin_InvalidPassword() {
        String email = "test@example.com";
        String password = "wrongPassword";
        String encryptedPassword = passwordEncoder.encode("correctPassword");
        User user = new User();
        user.setEmail(email);
        user.setPassword(encryptedPassword);

        AuthenticationDTO authData = new AuthenticationDTO(email, password);

        when(userRepository.findByEmail(email)).thenReturn(user);

        Exception exception = assertThrows(Exception.class, () -> authService.login(authData));
        assertEquals("Senha inválida", exception.getMessage());
    }

   /*@Test
    void testCreateUser() {
        RegisterDTO registerData = new RegisterDTO("Teste User", "teste@mail.com.br", "3499999999",
                "test@example.com", "Test Company",
                "12345678901", "285595959", UserRole.USER);
        String encryptedPassword = passwordEncoder.encode("password");

        authService.createUser(registerData, encryptedPassword);

        verify(userRepository, times(1)).save(any(User.class));
    }*/

    @Test
    void testGetUserById_UserFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = authService.getUserById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
    }

    @Test
    void testGetUserById_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User foundUser = authService.getUserById(userId);

        assertNull(foundUser);
    }

    @Test
    void testGetRole_UserFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setRole(UserRole.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserRole role = authService.getRole(userId);

        assertEquals(UserRole.USER, role);
    }

    @Test
    void testGetRole_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.getRole(userId));
        assertEquals("Usuário não encontrado!", exception.getMessage());
    }
}
