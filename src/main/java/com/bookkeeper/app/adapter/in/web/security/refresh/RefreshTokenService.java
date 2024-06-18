package com.bookkeeper.app.adapter.in.web.security.refresh;

import com.bookkeeper.app.adapter.out.persistance.RefreshTokenRepository;
import io.vavr.control.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class RefreshTokenService {

  private static final Logger LOG = LogManager.getLogger(RefreshTokenService.class);

  private final RefreshTokenRepository refreshTokenRepository;
  private final long refreshTokenExpiration;

  public RefreshTokenService(
      RefreshTokenRepository refreshTokenRepository, long refreshTokenExpiration) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.refreshTokenExpiration = refreshTokenExpiration;
  }

  public RefreshToken createRefreshToken(String email) {
    if (email == null || email.isEmpty()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }

    LOG.info("Generating refresh token for user {}", email);
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
    LOG.info("Checking refresh token validity for {}", refreshToken.email());
    return refreshToken.expiration().compareTo(new Date(Instant.now().toEpochMilli())) > 0;
  }
}
