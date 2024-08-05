package com.bookkeeper.app.application.domain.model;

import java.io.File;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Photo extends Event {
  private final String description;
  private final File photo;

  public Photo(EventId id, String description, File photo, EventDate date, EventCreator creator) {
    super(id, date, creator);
    this.description = description;
    this.photo = photo;
  }

  public Photo(String description, File photo, EventDate date, EventCreator creator) {
    super(EventId.random(), date, creator);
    this.description = description;
    this.photo = photo;
  }
}
