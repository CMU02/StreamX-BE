package a4.streamx_be.user.domain.entity;

import a4.streamx_be.user.domain.model.Provider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @Column(name = "user_uid")
    private UUID uid;

    @Column(nullable = false, name = "display_name")
    private String displayName;

    @Column(nullable = false, unique = true ,name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(name = "photo_url")
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider; // GOOGLE, LOCAL, NAVER, KAKAO

    @Column(name = "provider_id")
    private String providerId;

    // 1:1 MetaData - PK 공유
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private MetaData metaData;

    // MemberShip(현재 플랜) - FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id")
    private MemberShip memberShip;

    // 사용량
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserUsage> usages = new ArrayList<>();

    // 플랜 변경 이력
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMemberShipHistory> memberShipHistories = new ArrayList<>();

    // 🔐 UserDetails 필수 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return getDisplayName();
    }
}