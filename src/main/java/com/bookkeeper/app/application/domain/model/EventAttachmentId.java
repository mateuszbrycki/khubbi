package com.bookkeeper.app.application.domain.model;

import java.util.UUID;

@ValueObject
public record EventAttachmentId(UUID value) {
  public static EventAttachmentId of(UUID value) {
    return new EventAttachmentId(value);
  }

  public static EventAttachmentId random() {
    return new EventAttachmentId(UUID.randomUUID());
  }
}
