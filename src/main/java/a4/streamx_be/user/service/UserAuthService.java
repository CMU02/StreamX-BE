package a4.streamx_be.user.service;

import a4.streamx_be.user.domain.dto.request.SignInRequest;
import a4.streamx_be.user.domain.dto.request.SignupRequest;

public interface UserAuthService {
    String signUp(SignupRequest request);
    String signIn(SignInRequest request);
}
