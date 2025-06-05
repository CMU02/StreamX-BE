package a4.streamx_be.user.controller;

import a4.streamx_be.user.domain.dto.request.SignInRequest;
import a4.streamx_be.user.domain.dto.request.SignupRequest;
import a4.streamx_be.user.domain.dto.response.TokenResponse;
import a4.streamx_be.user.domain.dto.response.UserProfileResponse;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.service.UserAuthService;
import a4.streamx_be.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserAuthService userAuthService;
    private final UserService userService;

    @GetMapping("/user/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserProfile(user));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
        return ResponseEntity.ok(userAuthService.signUp(request));
    }

    @PostMapping("/auth/signIn")
    public ResponseEntity<TokenResponse> signIn(@RequestBody @Valid SignInRequest request) {
        String token = userAuthService.signIn(request);
        return ResponseEntity.ok(new TokenResponse(token));
    }
}