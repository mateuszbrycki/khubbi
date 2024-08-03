package com.bookkeeper.app.adapter.in.web.security.refresh;

import com.bookkeeper.app.adapter.out.persistance.RefreshTokenRepository;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final long refreshTokenExpiration;

  public RefreshToken createRefreshToken(String email) {
    if (email == null || email.isEmpty()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }

    log.info("Generating refresh token for user {}", email);
    RefreshToken refreshToken =
        new RefreshToken(
            UUID.randomUUID().toString(),
            new Date(System.currentTimeMillis() + this.refreshTokenExpiration),
            email);

    refreshTokenRepository.saveRefreshToken(refreshToken);

    return refreshToken;
  }

  public Option<RefreshToken> findToken(String token) {
    return refreshTokenRepository.getRefreshTokenByToken(token);
  }

  public Boolean isTokenValid(RefreshToken refreshToken) {
    log.info("Checking refresh token validity for {}", refreshToken.email());
    return refreshToken.expiration().compareTo(new Date(Instant.now().toEpochMilli())) > 0;
  }

  public Try<Boolean> invalidateUserTokens(String email) {
    log.info("Invalidating Refresh Tokens for {}", email);
    return Try.of(() -> this.refreshTokenRepository.removeTokens(email));
  }
}
