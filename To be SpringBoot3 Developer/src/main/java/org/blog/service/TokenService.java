package org.blog.service;

import lombok.RequiredArgsConstructor;
import org.blog.config.jwt.TokenProvider;
import org.blog.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;

    private final RefreshTokenService refreshTokenService; // todo : 같은 폴더 동작 방법 이해하기
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected access token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));

    }
}
