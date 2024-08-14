package com.bookkeeper.app.application.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Photo extends Event {
  private final String description;
  private final EventAttachment.PhotoAttachment photo;

  Photo(
      EventId id,
      String description,
      EventAttachment.PhotoAttachment photo,
      EventDate date,
      EventCreator creator) {
    super(id, date, creator);
    this.description = description;
    this.photo = photo;
  }

  public static Photo create(
      String description,
      EventAttachment.PhotoAttachment photo,
      EventDate date,
      EventCreator creator) {
    return new Photo(EventId.random(), description, photo, date, creator);
  }
}
