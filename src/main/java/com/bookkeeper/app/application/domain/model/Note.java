package com.bookkeeper.app.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Note extends Event {

  private final String note;

  public Note(EventId id, String note, EventDate date, EventCreator creator) {
    super(id, date, creator);
    this.note = note;
  }

  public static Note create(String note, EventDate date, EventCreator creator) {
    return new Note(EventId.random(), note, date, creator);
  }
}
