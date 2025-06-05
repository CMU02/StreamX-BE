package a4.streamx_be.util;

import a4.streamx_be.user.domain.model.Provider;
import org.springframework.stereotype.Component;

@Component
public class SignInUtils {

    /**
     * 주어진 문자열 기반 provider 값을 Enum 타입 Provider로 매핑하여 반환합니다. <br />
     * 예시 <br/>
     * - "google" -> Provider.GOOGLE <br/>
     * - "naver" -> Provider.NAVER <br/>
     * - "kakao" -> Provider.KAKAO <br/>
     * - 알수 없는 값 또는 비밀번호 -> Provider.LOCAL <br/>
     * @param provider OAuth2 로그인 시 받아온 provider 식별자 문자열 (예: "google", "naver", "kakao")
     * @return 해당 provider에 대응되는 Provider enum 값
     */
    public Provider getSignedInProvider(String provider) {
        return switch (provider) {
            case "google" -> Provider.GOOGLE;
            case "naver" -> Provider.NAVER;
            case "kakao" -> Provider.KAKAO;
            default -> Provider.LOCAL;
        };
    }

    /**
     * 비밀번호가 최소 8자 이상이며, 특수문자를 반드시 포함하는 검증
     * @param password 입력 비밀번호
     * @return 유효하면 true, 아니면 false
     */
    public boolean isValidPassword(String password) {
        // 특수문자 정의 (원하는 문자에 따라 추가/제거 가능)
        String specialChars = "!@#$%^&*()_+\\-=`~\\[\\]{};':\"\\\\|,.<>/?";
        // 정규식: 최소 8자, 하나 이상 특수문자 포함
        String pattern = "^(?=.*[" + specialChars + "]).{8,}$";
        return password != null && password.matches(pattern);
    }
}
