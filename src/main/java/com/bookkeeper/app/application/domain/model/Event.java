package com.bookkeeper.app.application.domain.model;

import java.util.Objects;

public abstract class Event {

  private final EventId id;
  private final EventDate date;
  private final EventCreator owner;

  protected Event(EventId id, EventDate date, EventCreator owner) {
    this.id = id;
    this.date = date;
    this.owner = owner;
  }

  public EventId getId() {
    return id;
  }

  public EventDate getDate() {
    return date;
  }

  public EventCreator getCreator() {
    return owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return Objects.equals(id, event.id)
        && Objects.equals(date, event.date)
        && Objects.equals(owner, event.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, date, owner);
  }
}
