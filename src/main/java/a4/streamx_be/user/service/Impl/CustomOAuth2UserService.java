package a4.streamx_be.user.service.Impl;

import a4.streamx_be.exception.ErrorCode;
import a4.streamx_be.exception.NotFoundException;
import a4.streamx_be.user.domain.entity.MemberShip;
import a4.streamx_be.user.domain.entity.MetaData;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.domain.model.MemberShipType;
import a4.streamx_be.user.repository.MemberShipRepository;
import a4.streamx_be.user.repository.UserRepository;
import a4.streamx_be.util.SignInUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final MemberShipRepository memberShipRepository;
    private final SignInUtils signInUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId(); // e.g. google
        String providerId = (String) attributes.get("sub");
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String picture = attributes.get("picture") != null ? attributes.get("picture").toString() : null;

        if (email == null) {
            throw new OAuth2AuthenticationException("이메일 정보를 받아올 수 없습니다.");
        }

        // ✅ 기존 유저 존재 여부 확인
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            // 저장하지 않고 그대로 반환 (단, 메타데이터 갱신 등은 필요시 추가 가능)
        } else {
            user = oauth2UserBuilder(provider, providerId, name, email, picture);
            userRepository.save(user);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "sub"
        );
    }

    private User oauth2UserBuilder(String provider, String providerId, String name, String email, String picture) {
        MemberShip memberShip = memberShipRepository.findByMemberShipType(MemberShipType.FREE)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        UUID userUid = UUID.randomUUID();
        String oauth2Password = passwordEncoder.encode("OAUTH2_USER");

        User oauthUser = User.builder()
                .uid(userUid)
                .displayName(name)
                .email(email)
                .providerId(providerId)
                .provider(signInUtils.getSignedInProvider(provider))
                .password(oauth2Password)
                .phoneNumber(null)
                .photoUrl(picture)
                .memberShip(memberShip)
                .build();

        MetaData metaData = MetaData.builder()
                .user(oauthUser)
                .creationTime(Instant.now())
                .lastSignInTime(Instant.now())
                .build();

        oauthUser.assignMetaData(metaData);
        return oauthUser;
    }
}