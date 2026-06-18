package university.spring_lab3_notifications;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import university.spring_lab3_notifications.service.NotificationService;
import university.spring_lab3_notifications.controller.NotificationController;
import university.spring_lab3_notifications.repository.UserRepository;
import university.spring_lab3_notifications.security.JwtAuthenticationFilter;
import university.spring_lab3_notifications.security.CustomUserDetailsService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldReturnOkForGetAllNotifications() throws Exception {
        when(notificationService.getAllNotifications()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/notifications/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(notificationService, times(1)).getAllNotifications();
    }

    @Test
    void shouldReturnStringMessageOnDelete() throws Exception {
        Long notificationId = 1L;

        mockMvc.perform(delete("/notifications/" + notificationId))
                .andExpect(status().isOk())
                .andExpect(content().string("Уведомление удалено"));

        verify(notificationService, times(1)).deleteNotification(notificationId);
    }
}