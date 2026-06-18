package university.spring_lab3_notifications.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import university.spring_lab3_notifications.model.dto.LoginRequest;
import university.spring_lab3_notifications.security.JwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class JwtAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        return jwtService.generateToken(request.getEmail());
    }
}
