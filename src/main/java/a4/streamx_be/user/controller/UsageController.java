package a4.streamx_be.user.controller;

import a4.streamx_be.user.domain.dto.response.UsageResponse;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.service.UsageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UsageController {

    private final UsageQueryService usageQueryService;

    @GetMapping("/user/usage")
    public ResponseEntity<UsageResponse> getUserUsage(@AuthenticationPrincipal User user) {
        UsageResponse result = usageQueryService.getUsage(user);
        return ResponseEntity.ok(result);
    }
}
