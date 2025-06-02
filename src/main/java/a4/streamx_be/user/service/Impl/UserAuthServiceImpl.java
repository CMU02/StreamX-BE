package a4.streamx_be.user.service.Impl;

import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.NotFoundException;
import a4.streamx_be.user.domain.dto.request.SignInRequest;
import a4.streamx_be.user.domain.dto.request.SignupRequest;
import a4.streamx_be.user.domain.entity.MemberShip;
import a4.streamx_be.user.domain.entity.MetaData;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.domain.model.MemberShipType;
import a4.streamx_be.user.domain.model.Provider;
import a4.streamx_be.user.jwt.JwtTokenProvider;
import a4.streamx_be.user.repository.MemberShipRepository;
import a4.streamx_be.user.repository.UserRepository;
import a4.streamx_be.user.service.UserAuthService;
import a4.streamx_be.util.SignInUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final MemberShipRepository memberShipRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignInUtils signInUtils;
    private final JwtTokenProvider provider;


    @Override
    @Transactional
    public String signUp(SignupRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresent(user -> { throw new NotFoundException(ErrorCode.EMAIL_ALREADY_EXISTS);});

        User user = userBuilder(request);
        userRepository.save(user);

        return "회원 가입 성공";
    }

    @Override
    public String signIn(SignInRequest request) {
        User findUser = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        // 평문 비밀번호와 암호화된 비밀번호를 비교
        matchPassword(request.password(), findUser.getPassword());

        // 비밀번호가 일치하면 토큰 발급 및 성공 처리
        return provider.generateToken(findUser.getEmail());
    }

    private User userBuilder(SignupRequest request) {
        MemberShip memberShip = memberShipRepository.findByMemberShipType(MemberShipType.FREE)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        // 비밀번호 검증: 유효하지 않으면 예외
        if (!signInUtils.isValidPassword(request.password())) {
            throw new NotFoundException(ErrorCode.WEAK_PASSWORD);
        }

        UUID user_uid = UUID.randomUUID();
        String encodePassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .uid(user_uid)
                .displayName(request.username())
                .email(request.email())
                .password(encodePassword)
                .phoneNumber(request.phoneNumber())
                .photoUrl(null)
                .provider(Provider.LOCAL)
                .providerId(request.email()) // 일반 이메일 가입자는 providerId는 이메일로 기입
                .memberShip(memberShip)
                .build();

        // metadata
        MetaData metaData = MetaData.builder()
                .user(user)
                .creationTime(Instant.now())
                .lastSignInTime(Instant.now())
                .build();

        user.assignMetaData(metaData); // 관계 주입

        return user;
    }

    /**
     * 사용자가 입력한 평문 비밀번호와 DB에 저장된 인코딩된(암호화된) 비밀번호가 일치하는지 검사합니다.
     *
     * @param password         사용자가 입력한 평문 비밀번호 (null일 경우 예외 발생)
     * @param encodedPassword  DB에 저장된 암호화된 비밀번호
     * @return                 비밀번호가 일치하면 true (일치하지 않거나 필수 값이 누락되면 예외 발생)
     * @throws NotFoundException 비밀번호가 null 이거나 일치하지 않을 때 각각 REQUIRED_FIELD_MISSING 또는 PASSWORD_MISMATCH 에러코드로 예외 발생
     */
    private void matchPassword(String password, String encodedPassword) {
        if (password == null) {
            throw new NotFoundException(ErrorCode.REQUIRED_FIELD_MISSING);
        }
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new NotFoundException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
