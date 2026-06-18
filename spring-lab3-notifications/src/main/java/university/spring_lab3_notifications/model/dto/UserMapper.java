package university.spring_lab3_notifications.model.dto;

import org.springframework.stereotype.Component;

import university.spring_lab3_notifications.model.entity.User;

@Component
public class UserMapper {

    public UserDto toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .deviceToken(user.getDeviceToken())
                .telegramChatId(user.getTelegramChatId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}