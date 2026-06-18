package university.spring_lab3_notifications.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import university.spring_lab3_notifications.model.dto.NotificationDto;
import university.spring_lab3_notifications.model.entity.Notification;
import university.spring_lab3_notifications.model.entity.User;
import university.spring_lab3_notifications.model.enums.NotificationChannel;
import university.spring_lab3_notifications.model.enums.NotificationStatus;
import university.spring_lab3_notifications.repository.NotificationRepository;
import university.spring_lab3_notifications.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private NotificationDto mapToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus());
        dto.setChannel(notification.getChannel());
        dto.setRecipientId(notification.getRecipient().getId());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setSentAt(notification.getSentAt());
        return dto;
    }

    @Transactional
    public NotificationDto createNotification(NotificationDto request) {
        if (!userRepository.existsById(request.getRecipientId())) {
            throw new EntityNotFoundException("Пользователь с id " + request.getRecipientId() + " не найден");
        }
        User user = userRepository.findById(request.getRecipientId()).orElseThrow();

        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setChannel(request.getChannel());
        notification.setStatus(NotificationStatus.CREATED);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRecipient(user);

        Notification saved = notificationRepository.save(notification);
        return mapToDto(saved);
    }  
     
    public List<NotificationDto> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public NotificationDto getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        return mapToDto(notification);
    }

    public NotificationDto updateNotification(Long id, NotificationDto request)
    {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setChannel(request.getChannel());
        if (request.getStatus() != null) {
            notification.setStatus(request.getStatus());
        }
        if (request.getStatus() == NotificationStatus.SENT && notification.getSentAt() == null) {
            notification.setSentAt(LocalDateTime.now());
        }

        Notification saved = notificationRepository.save(notification);
        return mapToDto(saved);
    }

    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notificationRepository.delete(notification);
    }

    public List<NotificationDto> getNotificationsByStatus(NotificationStatus status) {
        return notificationRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<NotificationDto> getNotificationsByChannel(NotificationChannel channel) {
        return notificationRepository.findByChannel(channel).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<NotificationDto> getNotificationsByRecipientId(Long recipientId) {
        return notificationRepository.findByRecipientId(recipientId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<NotificationDto> getByStatusAndChannel(NotificationStatus status, NotificationChannel channel) {
        return notificationRepository.findByStatusAndChannel(status, channel).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<NotificationDto> getAllSortedByDate() {
        return notificationRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<NotificationDto> getNotificationsByUserAndStatus(Long userId, NotificationStatus status) {
    return notificationRepository.findByRecipientAndStatus(userId, status)
            .stream()
            .map(this::mapToDto)
            .toList();
    }
}
