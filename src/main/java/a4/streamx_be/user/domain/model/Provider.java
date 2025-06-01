package a4.streamx_be.user.domain.model;

import lombok.Getter;

@Getter
public enum Provider {
    LOCAL, // 자체 로그인 또는 등록되지 않은 제공자
    GOOGLE,
    NAVER,
    KAKAO
}
