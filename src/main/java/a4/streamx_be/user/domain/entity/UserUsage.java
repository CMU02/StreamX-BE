package a4.streamx_be.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_uid", referencedColumnName = "user_uid", nullable = false)
    private User user;

    // 집계 단위: 일 (UTC)
    @Column(nullable = false, name = "usage_time")
    private Instant usageTime;

    // 일반 채팅 카운트
    @Column(nullable = false)
    private Long chatCount = 0L;

    // TTS 사용 카운트
    @Column(nullable = false)
    private Long ttsCount = 0L;

    public void changeChatCount(Long count) {
        chatCount += count;
    }

    public void changeTtsCount(Long count) {
        ttsCount += count;
    }
}
