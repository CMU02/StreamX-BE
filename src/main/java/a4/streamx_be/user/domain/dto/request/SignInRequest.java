package a4.streamx_be.user.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
