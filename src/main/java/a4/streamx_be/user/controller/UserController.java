package a4.streamx_be.user.controller;

import a4.streamx_be.user.domain.User;
import a4.streamx_be.user.dto.LoginRequest;
import a4.streamx_be.user.dto.SignupRequest;
import a4.streamx_be.user.service.UserAuthService;
import a4.streamx_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserAuthService userAuthService;

    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("picture", user.getPicture());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        return userAuthService.signup(request);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userAuthService.login(request);
    }
}