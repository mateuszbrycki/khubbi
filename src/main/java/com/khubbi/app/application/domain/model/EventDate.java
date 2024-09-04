package com.khubbi.app.application.domain.model;

import com.khubbi.app.validation.Errors;
import io.vavr.control.Validation;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.Getter;

@ValueObject
@Getter
public class EventDate implements Comparable<EventDate> {

  private final ZonedDateTime value;

  private EventDate(ZonedDateTime value) {
    this.value = value;
  }

  public static Validation<Errors, EventDate> of(ZonedDateTime value) {
    return Validation.valid(new EventDate(value));
  }

  public static EventDate now() {
    return new EventDate(ZonedDateTime.now());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EventDate eventDate = (EventDate) o;
    return Objects.equals(value, eventDate.value);
  }

  @Override
  public int compareTo(EventDate o) {
    return this.value().compareTo(o.value());
  }
}
