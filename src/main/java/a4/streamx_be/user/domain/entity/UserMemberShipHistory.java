package a4.streamx_be.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMemberShipHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK -> User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    private User user;

    // FK -> MemberShip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", nullable = false)
    private MemberShip membership;

    // 시작 시각 (UTC)
    @Column(nullable = false, name = "start_at")
    private Instant startAt;

    // 종료 시각 - NULL = 현재 활성 구독
    @Column(name = "end_at")
    private Instant endAt;
}
