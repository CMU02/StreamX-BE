package a4.streamx_be.user.service.Impl;

import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.domain.model.Provider;
import a4.streamx_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String providerStr = userRequest.getClientRegistration().getRegistrationId(); // "google"
        String providerId = oAuth2User.getName(); // 구글의 sub
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("이메일 정보를 받아올 수 없습니다.");
        }

        // provider enum으로 변환
        Provider provider;
        try {
            provider = Provider.valueOf(providerStr.toUpperCase()); // "google" → GOOGLE
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다: " + providerStr);
        }

        // ✅ 기존 유저 존재 여부 확인
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            // 필요 시 업데이트 로직 추가 가능
        } else {
            user = User.builder()
                    .uid(UUID.randomUUID())
                    .email(email)
                    .displayName(name)
                    .provider(provider)
                    .providerId(providerId)
                    .photoUrl(picture)
                    .password(UUID.randomUUID().toString()) // 소셜 로그인은 임시 비밀번호
                    .build();

            userRepository.save(user);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "sub" // 기본 사용자 식별 키
        );
    }
}