package university.spring_lab3_notifications.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import university.spring_lab3_notifications.service.NotificationManager;

@RestController
public class NotificationController {

    private final NotificationManager notificationManager;

    public NotificationController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @GetMapping("/notify")
    public String notify(
        @RequestParam String message, 
        @RequestParam String email,
        @RequestParam(required = false, defaultValue = "emailService") String type
    ) {
        notificationManager.notify(message, email, type);
        return "Запрос на отправку через " + type + " принят!";
    }
}
