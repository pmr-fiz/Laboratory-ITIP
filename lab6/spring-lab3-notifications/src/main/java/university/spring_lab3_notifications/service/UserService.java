package university.spring_lab3_notifications.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import university.spring_lab3_notifications.model.dto.UserDto;
import university.spring_lab3_notifications.model.dto.UserMapper;
import university.spring_lab3_notifications.model.entity.User;
import university.spring_lab3_notifications.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDeviceToken(request.getDeviceToken());
        user.setTelegramChatId(request.getTelegramChatId());
        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
        return userMapper.toResponse(user);
    }

    public UserDto updateUser(Long id, UserDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
        
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDeviceToken(request.getDeviceToken());
        user.setTelegramChatId(request.getTelegramChatId());
        
        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден"));
        userRepository.delete(user);
    }
}