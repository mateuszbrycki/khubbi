package com.bookkeeper.app.application.domain.model;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Note extends Event {

  private final String note;

  public Note(String note, EventDate date, EventCreator creator) {
    super(EventId.of(UUID.randomUUID()), date, creator);
    this.note = note;
  }
}
