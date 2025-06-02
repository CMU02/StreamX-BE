package a4.streamx_be.user.service.Impl;

import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.NotFoundException;
import a4.streamx_be.user.domain.dto.response.UserProfileResponse;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserProfileResponse getUserProfile(User user) {
        if (user == null) {
            throw new NotFoundException(ErrorCode.NOT_AUTHENTICATED_USER);
        }

        return new UserProfileResponse(user);
    }
}
