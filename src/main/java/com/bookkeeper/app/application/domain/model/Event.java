package com.bookkeeper.app.application.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Event {

  private final UUID id;
  private final String note;
  private final LocalDateTime date;
  private final User owner;

  public Event(String note, LocalDateTime date, User owner) {
    this.owner = owner;
    this.id = UUID.randomUUID();
    this.note = note;
    this.date = date;
  }

  public UUID getId() {
    return id;
  }

  public String getNote() {
    return note;
  }

  public User getOwner() {
    return owner;
  }

  public LocalDateTime getDate() { return date;}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return Objects.equals(id, event.id)
        && Objects.equals(note, event.note)
        && Objects.equals(owner, event.owner)
        && Objects.equals(date, event.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, note, date, owner);
  }
}
