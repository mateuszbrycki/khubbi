package com.bookkeeper.app.adapter.out.persistance;

import io.vavr.collection.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;

public class UserTokenRepository {
  private static final Logger LOG = LogManager.getLogger(UserTokenRepository.class);

  private Map<UserDetails, String> activeUserTokens;

  public UserTokenRepository(Map<UserDetails, String> activeUserTokens) {
    this.activeUserTokens = activeUserTokens;
  }

  public Boolean isTokenActive(UserDetails user, String token) {
    LOG.info("Checking if {} used active token.", user.getUsername());
    return this.activeUserTokens.containsKey(user)
        && this.activeUserTokens.get(user).get().equals(token);
  }

  public Boolean refreshToken(UserDetails user, String token) {
    LOG.info("Refreshing token for {}", user.getUsername());
    this.activeUserTokens = this.activeUserTokens.put(user, token);
    // TODO mateusz.brycki fix that
    return true;
  }

  public Boolean removeTokens(String email) {
    this.activeUserTokens =
        this.activeUserTokens.filter(
            (UserDetails key, String value) -> !key.getUsername().equals(email));

    // TODO mateusz.brycki fix that
    return true;
  }
}
