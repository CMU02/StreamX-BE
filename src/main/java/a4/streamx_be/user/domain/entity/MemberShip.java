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
    // Free : 45, Pro : Unlimit
    @Column(name = "daily_chat_limit")
    private Long dailyChatLimit;

    // 일일 TTS 한도 : 20 (Pro 한정)
    @Column(name = "daily_tts_limit")
    private Long dailyTtsLimit;

    // 월 정가 (원화, VAT 별도)
    @ColumnDefault("0")
    @Column(nullable = false, name = "monthly_price")
    private Long monthlyPrice;
}
