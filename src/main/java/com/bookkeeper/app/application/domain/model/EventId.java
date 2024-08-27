package com.bookkeeper.app.application.domain.model;

import com.bookkeeper.app.validation.Errors;
import io.vavr.control.Validation;
import java.util.UUID;
import lombok.Getter;

@ValueObject
@Getter
public class EventId {

  private final UUID value;

  private EventId(UUID value) {
    this.value = value;
  }

  public static Validation<Errors, EventId> of(UUID value) {
    return Validation.valid(new EventId(value));
  }

  public static EventId random() {
    return new EventId(UUID.randomUUID());
  }
}
