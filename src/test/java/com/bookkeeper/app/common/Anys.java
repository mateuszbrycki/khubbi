package com.bookkeeper.app.common;

import com.bookkeeper.app.application.domain.model.User;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Anys {

  public static final String ANY_EMAIL = "any-email";

  public static final User ANY_USER =
      new User(
          UUID.randomUUID(),
          "any-fullname",
          ANY_EMAIL,
          "any-password",
          Date.from(Instant.now()),
          Date.from(Instant.now()));
}
