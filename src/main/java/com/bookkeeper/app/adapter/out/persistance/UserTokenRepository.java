package com.bookkeeper.app.adapter.out.persistance;

import io.vavr.collection.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@AllArgsConstructor
public class UserTokenRepository {

  private Map<UserDetails, String> activeUserTokens;

  public Boolean isTokenActive(UserDetails user, String token) {
    log.info("Checking if {} used active token.", user.getUsername());
    return this.activeUserTokens.containsKey(user)
        && this.activeUserTokens.get(user).get().equals(token);
  }

  public Boolean refreshToken(UserDetails user, String token) {
    log.info("Refreshing token for {}", user.getUsername());
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
