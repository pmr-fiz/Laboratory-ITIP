package university.spring_lab3_notifications;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import university.spring_lab3_notifications.model.dto.UserDto;
import university.spring_lab3_notifications.model.dto.UserMapper;
import university.spring_lab3_notifications.model.entity.User;
import university.spring_lab3_notifications.repository.UserRepository;
import university.spring_lab3_notifications.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUserById() {
        Long userId = 1L;
        
        User user = new User();
        user.setId(userId);
        user.setName("Иван");
        user.setEmail("ivan@example.com");
        user.setCreatedAt(LocalDateTime.now());

        UserDto expectedDto = UserDto.builder()
                .name("Иван")
                .email("ivan@example.com")
                .createdAt(user.getCreatedAt())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expectedDto);

        UserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("Иван", result.getName());
        assertEquals("ivan@example.com", result.getEmail());
        
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    void shouldDeleteUserAndVerifyRepositoryCall() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }
}