package a4.streamx_be.user.domain.entity;

import a4.streamx_be.user.domain.model.MemberShipType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberShip {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, unique = true)
    private MemberShipType memberShipType = MemberShipType.FREE; // "FREE", "PRO"

    // 일일 텍스트 채팅 한도 (-1 == Unlimit)
    // Free : 300, Pro : -1
    @Column(name = "weekly_chat_limit")
    private Long weeklyChatLimit;

    // 일일 TTS 한도 : 140회 (Pro 한정)
    @Column(name = "weekly_tts_limit")
    private Long weeklyTtsLimit;

    // 월 정가 (원화, VAT 별도)
    @ColumnDefault("0")
    @Column(nullable = false, name = "monthly_price")
    private Long monthlyPrice;
}
