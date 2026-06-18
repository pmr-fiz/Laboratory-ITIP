package university.spring_lab3_notifications;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import university.spring_lab3_notifications.model.dto.NotificationDto;
import university.spring_lab3_notifications.model.enums.NotificationChannel;
import university.spring_lab3_notifications.repository.NotificationRepository;
import university.spring_lab3_notifications.repository.UserRepository;
import university.spring_lab3_notifications.service.NotificationService;

@ExtendWith(MockitoExtension.class)
class NotificationServiceExceptionTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        NotificationDto dto = NotificationDto.builder()
                .title("Напоминание")
                .message("Сообщение")
                .channel(NotificationChannel.EMAIL)
                .recipientId(99L)
                .build();

        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            notificationService.createNotification(dto);
        });
    }
}
