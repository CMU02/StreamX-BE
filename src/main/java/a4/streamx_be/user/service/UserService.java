package a4.streamx_be.user.service;

import a4.streamx_be.user.domain.dto.response.UserProfileResponse;
import a4.streamx_be.user.domain.entity.User;

public interface UserService {
    UserProfileResponse getUserProfile(User user);
}
