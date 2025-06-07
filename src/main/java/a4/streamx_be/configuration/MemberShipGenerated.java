package a4.streamx_be.configuration;

import a4.streamx_be.user.domain.entity.MemberShip;
import a4.streamx_be.user.domain.entity.MetaData;
import a4.streamx_be.user.domain.entity.User;
import a4.streamx_be.user.domain.model.MemberShipType;
import a4.streamx_be.user.domain.model.Provider;
import a4.streamx_be.user.repository.MemberShipRepository;
import a4.streamx_be.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberShipGenerated {

    private final MemberShipRepository memberShipRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void memberShipGenerated() {

        MemberShip freeMembership = MemberShip.builder()
                .memberShipType(MemberShipType.FREE)
                .weeklyChatLimit(300L)
                .weeklyTtsLimit(0L)
                .monthlyPrice(0L)
                .build();

        MemberShip ProMembership = MemberShip.builder()
                .memberShipType(MemberShipType.PRO)
                .weeklyChatLimit(-1L)
                .weeklyTtsLimit(140L)
                .monthlyPrice(15000L)
                .build();

        memberShipRepository.save(freeMembership);
        memberShipRepository.save(ProMembership);

        User user = User.builder()
                .memberShip(ProMembership)
                .email("tester@gmail.com")
                .uid(UUID.randomUUID())
                .password(passwordEncoder.encode("!miss1234"))
                .phoneNumber("010-1234-5678")
                .provider(Provider.LOCAL)
                .photoUrl(null)
                .providerId("tester@gmail.com")
                .displayName("tester")
                .build();

        MetaData metaData = MetaData.builder()
                .creationTime(Instant.now())
                .lastSignInTime(Instant.now())
                .user(user)
                .build();

        user.assignMetaData(metaData);
        userRepository.save(user);
    }
}
