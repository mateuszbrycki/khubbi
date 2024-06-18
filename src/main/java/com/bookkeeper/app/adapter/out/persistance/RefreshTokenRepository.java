package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.adapter.in.web.security.refresh.RefreshToken;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RefreshTokenRepository {

  private static final Logger LOG = LogManager.getLogger(RefreshTokenRepository.class);
  private Map<String, RefreshToken> refreshTokens;

  public RefreshTokenRepository(Map<String, RefreshToken> refreshTokens) {
    this.refreshTokens = refreshTokens;
  }

  public Option<RefreshToken> getRefreshTokenByToken(String token) {
    LOG.info("Retrieving refresh token");
    return this.refreshTokens.get(token);
  }

  public void saveRefreshToken(RefreshToken refreshToken) {
    LOG.info("Saving refresh token for {}", refreshToken.email());
    this.refreshTokens = this.refreshTokens.put(refreshToken.token(), refreshToken);
  }
}
