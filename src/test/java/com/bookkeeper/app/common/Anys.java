package com.bookkeeper.app.common;

import static org.mockito.Mockito.mock;

import com.bookkeeper.app.application.domain.model.EventCreator;
import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.User;
import java.io.File;
import java.time.Instant;
import java.time.ZonedDateTime;
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

  public static final EventCreator ANY_EVENT_CREATOR = EventCreator.of(ANY_USER);

  public static final User ANY_ANOTHER_USER =
      new User(
          UUID.randomUUID(),
          "any-fullname-2",
          ANY_EMAIL,
          "any-password-2",
          Date.from(Instant.now()),
          Date.from(Instant.now()));

  public static final EventCreator ANY_ANOTHER_EVENT_CREATOR = EventCreator.of(ANY_ANOTHER_USER);

  public static final com.bookkeeper.app.adapter.in.web.security.User ANY_SECURITY_USER =
      new com.bookkeeper.app.adapter.in.web.security.User(
          UUID.randomUUID(),
          "any-fullname",
          ANY_EMAIL,
          "any-password",
          Date.from(Instant.now()),
          Date.from(Instant.now()));

  public static final EventDate ANY_EVENT_DATE = EventDate.of(ZonedDateTime.now());

  public static final File ANY_FILE = mock(File.class);
  public static final String ANY_FILE_PATH = "any-file-path";
}
