package a4.streamx_be.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MetaData {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne @MapsId
    @JoinColumn(name = "user_uid")
    private User user;

    @CreationTimestamp
    @Column(name = "creation_time")
    private Instant creationTime; // 생성 시간

    @Column(columnDefinition = "TIMESTAMP")
    private Instant lastSignTime; // 마지막 로그인 시간

    @UpdateTimestamp
    @Column(name = "update_time")
    private Instant updateTime; // 계정 업데이트 시간
}
