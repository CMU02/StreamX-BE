package a4.streamx_be.configuration;

import a4.streamx_be.user.domain.entity.MemberShip;
import a4.streamx_be.user.domain.model.MemberShipType;
import a4.streamx_be.user.repository.MemberShipRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberShipGenerated {

    private final MemberShipRepository memberShipRepository;

    @PostConstruct
    public void memberShipGenerated() {

        MemberShip freeMembership = MemberShip.builder()
                .memberShipType(MemberShipType.FREE)
                .dailyChatLimit(45L)
                .dailyTtsLimit(0L)
                .monthlyPrice(0L)
                .build();

        MemberShip ProMembership = MemberShip.builder()
                .memberShipType(MemberShipType.PRO)
                .dailyChatLimit(-1L)
                .dailyTtsLimit(20L)
                .monthlyPrice(15000L)
                .build();

        memberShipRepository.save(freeMembership);
        memberShipRepository.save(ProMembership);
    }
}
