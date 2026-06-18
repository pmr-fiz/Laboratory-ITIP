package university.spring_lab3_notifications;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import university.spring_lab3_notifications.model.dto.NotificationDto;
import university.spring_lab3_notifications.model.entity.Notification;
import university.spring_lab3_notifications.model.entity.User;
import university.spring_lab3_notifications.model.enums.NotificationChannel;
import university.spring_lab3_notifications.model.enums.NotificationStatus;
import university.spring_lab3_notifications.repository.NotificationRepository;
import university.spring_lab3_notifications.service.NotificationService;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldReturnNotificationById() {
        Long notificationId = 10L;

        User recipient = new User();
        recipient.setId(1L);

        Notification notification = new Notification();
        notification.setId(notificationId);
        notification.setTitle("Важное уведомление");
        notification.setMessage("Текст сообщения");
        notification.setStatus(NotificationStatus.CREATED);
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setRecipient(recipient);
        notification.setCreatedAt(LocalDateTime.now());

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        NotificationDto result = notificationService.getNotificationById(notificationId);

        assertNotNull(result);
        assertEquals("Важное уведомление", result.getTitle());
        assertEquals("Текст сообщения", result.getMessage());
        assertEquals(NotificationStatus.CREATED, result.getStatus());
        assertEquals(1L, result.getRecipientId());

        verify(notificationRepository, times(1)).findById(notificationId);
    }

    @Test
    void shouldThrowExceptionWhenNotificationNotFound() {
        Long notificationId = 99L;
        
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            notificationService.getNotificationById(notificationId);
        });

        verify(notificationRepository, times(1)).findById(notificationId);
    }
}