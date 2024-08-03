package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.adapter.in.web.security.refresh.RefreshToken;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RefreshTokenRepository {

  private Map<String, RefreshToken> refreshTokens;

  public Option<RefreshToken> getRefreshTokenByToken(String token) {
    log.info("Retrieving refresh token");
    return this.refreshTokens.get(token);
  }

  public void saveRefreshToken(RefreshToken refreshToken) {
    log.info("Saving refresh token for {}", refreshToken.email());
    this.refreshTokens = this.refreshTokens.put(refreshToken.token(), refreshToken);
  }

  public Boolean removeTokens(String email) {
    this.refreshTokens =
        this.refreshTokens.filter(
            (String token, RefreshToken refreshToken) -> !refreshToken.email().equals(email));
    return true;
  }
}
