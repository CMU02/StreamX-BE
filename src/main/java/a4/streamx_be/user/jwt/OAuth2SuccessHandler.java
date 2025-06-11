package a4.streamx_be.user.jwt;

import a4.streamx_be.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");

        // JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(email);

        String scheme = request.getScheme();      // "http" or "https"
        String domain = request.getServerName();  // "stream.cieloblue.co.kr"
        int port = request.getServerPort();       // 80 or 443 or custom

        String baseUrl = scheme + "://" + domain +
                ((scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443) ? "" : ":" + port);
        // PC Test URL 작성
        String redirectUri = baseUrl + "/oauth/callback?token=" + token;

        response.sendRedirect(redirectUri);
    }
}