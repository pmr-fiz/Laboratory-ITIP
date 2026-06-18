package university.spring_lab3_notifications;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import university.spring_lab3_notifications.model.dto.RegisterRequest;
import university.spring_lab3_notifications.model.enums.UserRole;
import university.spring_lab3_notifications.repository.UserRepository;
import university.spring_lab3_notifications.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Алексей");
        request.setEmail("alex@example.com");
        request.setPassword("userPassword123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("userPassword123")).thenReturn("encodedUserPassword");

        authService.register(request);

        verify(passwordEncoder, times(1)).encode("userPassword123");
        verify(userRepository, times(1)).save(argThat(user -> 
                user.getName().equals("Алексей") &&
                user.getEmail().equals("alex@example.com") &&
                user.getPassword().equals("encodedUserPassword") &&
                user.getRole() == UserRole.ROLE_USER
        ));
    }

    @Test
    void shouldRegisterAdminSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Владимир");
        request.setEmail("admin@example.com");
        request.setPassword("adminPassword123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("adminPassword123")).thenReturn("encodedAdminPassword");

        authService.registerAdmin(request);

        verify(passwordEncoder, times(1)).encode("adminPassword123");
        verify(userRepository, times(1)).save(argThat(user -> 
                user.getName().equals("Владимир") &&
                user.getEmail().equals("admin@example.com") &&
                user.getPassword().equals("encodedAdminPassword") &&
                user.getRole() == UserRole.ROLE_ADMIN
        ));
    }
}
