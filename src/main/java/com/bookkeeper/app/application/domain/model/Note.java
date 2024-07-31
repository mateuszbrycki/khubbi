package com.bookkeeper.app.application.domain.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class Note {

  private final UUID id;
  private final String note;
  private final ZonedDateTime date;
  private final User owner;

  public Note(String note, ZonedDateTime date, User owner) {
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

  public ZonedDateTime getDate() {
    return date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Note note = (Note) o;
    return Objects.equals(id, note.id)
        && Objects.equals(this.note, note.note)
        && Objects.equals(owner, note.owner)
        && Objects.equals(date, note.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, note, date, owner);
  }
}
