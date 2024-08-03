package com.bookkeeper.app.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class Event {

  private final EventId id;
  private final EventDate date;
  private final EventCreator creator;

  protected Event(EventId id, EventDate date, EventCreator creator) {
    this.id = id;
    this.date = date;
    this.creator = creator;
  }
}
