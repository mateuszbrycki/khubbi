package com.bookkeeper.app.application.domain.model;

import java.time.ZonedDateTime;
import java.util.Objects;

public record EventDate(ZonedDateTime value) implements Comparable<EventDate> {

  public static EventDate of(ZonedDateTime value) {
    return new EventDate(value);
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
