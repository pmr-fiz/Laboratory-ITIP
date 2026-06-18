package university.spring_lab3_notifications.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import university.spring_lab3_notifications.model.dto.RegisterRequest;
import university.spring_lab3_notifications.model.entity.User;
import university.spring_lab3_notifications.model.enums.UserRole;
import university.spring_lab3_notifications.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Пользователь с email " + request.getEmail() + " уже зарегистрирован!");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void registerAdmin(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_ADMIN);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
