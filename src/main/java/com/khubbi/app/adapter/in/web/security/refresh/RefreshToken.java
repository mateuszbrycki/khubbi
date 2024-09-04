package com.khubbi.app.adapter.in.web.security.refresh;

import java.util.Date;

public record RefreshToken(String token, Date expiration, String email) {

  public long getExpirationTimeInMillis() {
    return this.expiration().toInstant().toEpochMilli();
  }
}
