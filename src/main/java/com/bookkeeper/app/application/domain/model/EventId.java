package com.bookkeeper.app.application.domain.model;

import java.util.UUID;

@ValueObject
public record EventId(UUID value) {

  public static EventId of(UUID value) {
    return new EventId(value);
  }

  public static EventId random() {
    return new EventId(UUID.randomUUID());
  }
}
