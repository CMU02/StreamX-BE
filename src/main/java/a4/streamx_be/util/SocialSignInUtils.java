package a4.streamx_be.util;

import a4.streamx_be.user.domain.model.Provider;
import org.springframework.stereotype.Component;

@Component
public class SocialSignInUtils {

    public Provider getSignedInProvider(String provider) {
        return switch (provider) {
            case "google" -> Provider.GOOGLE;
            case "naver" -> Provider.NAVER;
            case "kakao" -> Provider.KAKAO;
            default -> Provider.LOCAL;
        };
    }
}
