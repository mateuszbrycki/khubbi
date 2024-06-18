package com.bookkeeper.app.adapter.in.web.security.jwt;

import java.util.Date;

public class JwtToken {
  private final String token;
  private final Date expirationTime;

  public JwtToken(String token, Date expirationTime) {
    this.token = token;
    this.expirationTime = expirationTime;
  }

  public String getToken() {
    return token;
  }

  public Date getExpirationTime() {
    return expirationTime;
  }

  public long getExpirationTimeInMillis() {
    return this.getExpirationTime().toInstant().toEpochMilli();
  }
}
