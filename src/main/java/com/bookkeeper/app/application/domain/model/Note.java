package com.bookkeeper.app.application.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Note extends Event {

  private final String note;

  public Note(String note, EventDate date, EventCreator creator) {
    super(EventId.of(UUID.randomUUID()), date, creator);
    this.note = note;
  }

  public String getNote() {
    return note;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Note note1 = (Note) o;
    return Objects.equals(note, note1.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), note);
  }
}
