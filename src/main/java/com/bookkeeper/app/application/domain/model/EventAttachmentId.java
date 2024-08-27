package com.bookkeeper.app.application.domain.model;

import com.bookkeeper.app.validation.Errors;
import io.vavr.control.Validation;
import java.util.UUID;
import lombok.Getter;

@ValueObject
@Getter
public class EventAttachmentId {

  private final UUID value;

  private EventAttachmentId(UUID value) {
    this.value = value;
  }

  public static Validation<Errors, EventAttachmentId> of(UUID value) {
    return Validation.valid(new EventAttachmentId(value));
  }

  public static EventAttachmentId random() {
    return new EventAttachmentId(UUID.randomUUID());
  }
}
