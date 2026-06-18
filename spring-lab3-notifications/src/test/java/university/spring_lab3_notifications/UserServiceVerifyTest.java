package university.spring_lab3_notifications;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import university.spring_lab3_notifications.model.dto.UserDto;
import university.spring_lab3_notifications.model.dto.UserMapper;
import university.spring_lab3_notifications.model.entity.User;
import university.spring_lab3_notifications.repository.UserRepository;
import university.spring_lab3_notifications.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceVerifyTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCallSaveOnRepository() {
        UserDto dto = UserDto.builder()
                .name("Иван")
                .email("ivan@example.com")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.toResponse(any(User.class))).thenReturn(dto);

        userService.createUser(dto);

        verify(userRepository).save(any(User.class));
    }
}
