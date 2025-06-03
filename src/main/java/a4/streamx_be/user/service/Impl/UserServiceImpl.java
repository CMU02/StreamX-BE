package a4.streamx_be.user.service.Impl;

import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.NotFoundException;
import a4.streamx_be.user.domain.dto.response.UserProfileResponse;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.repository.UserRepository;
import a4.streamx_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserProfileResponse getUserProfile(User user) {
        if (user == null) {
            throw new NotFoundException(ErrorCode.NOT_AUTHENTICATED_USER);
        }
        User findUser = userRepository.findByUid(user.getUid())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return new UserProfileResponse(findUser);
    }
}
