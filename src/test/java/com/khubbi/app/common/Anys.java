package com.khubbi.app.common;

import static org.mockito.Mockito.mock;

import com.khubbi.app.adapter.in.web.security.User;
import com.khubbi.app.application.domain.model.EventAttachment;
import com.khubbi.app.application.domain.model.EventCreator;
import com.khubbi.app.application.domain.model.EventDate;
import com.khubbi.app.application.domain.model.UserEmail;
import java.io.File;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Anys {

  public static final String ANY_EMAIL = "any-email@any-email.com";
  public static final UserEmail ANY_USER_EMAIL = UserEmail.of(ANY_EMAIL).get();

  public static final com.khubbi.app.application.domain.model.User ANY_USER =
      com.khubbi.app.application.domain.model.User.builder()
          .id(UUID.randomUUID())
          .fullName("any-fullname")
          .email(ANY_EMAIL)
          .password("any-password")
          .createdAt(Date.from(Instant.now()))
          .updatedAt(Date.from(Instant.now()))
          .build();

  public static final EventCreator ANY_EVENT_CREATOR = EventCreator.of(ANY_USER);

  public static final com.khubbi.app.application.domain.model.User ANY_ANOTHER_USER =
      ANY_USER
          .withId(UUID.randomUUID())
          .withFullName("another-fullname")
          .withPassword("another-password");

  public static final EventCreator ANY_ANOTHER_EVENT_CREATOR = EventCreator.of(ANY_ANOTHER_USER);

  public static final User ANY_SECURITY_USER =
      User.builder()
          .id(UUID.randomUUID())
          .fullName("any-fullname")
          .email(ANY_EMAIL)
          .password("any-password")
          .createdAt(Date.from(Instant.now()))
          .updatedAt(Date.from(Instant.now()))
          .build();

  public static final EventDate ANY_EVENT_DATE = EventDate.now();

  public static final File ANY_FILE = mock(File.class);
  public static final String ANY_FILE_PATH = "any-file-path";

  public static final EventAttachment.PhotoAttachment ANY_PHOTO_ATTACHMENT =
      EventAttachment.PhotoAttachment.create(ANY_FILE);
}
