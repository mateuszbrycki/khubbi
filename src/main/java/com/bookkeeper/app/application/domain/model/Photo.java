package com.bookkeeper.app.application.domain.model;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class Photo extends Event {
  private final String description;
  private final File photo;

  public Photo(EventId id, String description, File photo, EventDate date, EventCreator creator) {
    super(id, date, creator);
    this.description = description;
    this.photo = photo;
  }

  public Photo(String description, File photo, EventDate date, EventCreator creator) {
    super(EventId.of(UUID.randomUUID()), date, creator);
    this.description = description;
    this.photo = photo;
  }

  public String getDescription() {
    return description;
  }

  public File getPhoto() {
    return photo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Photo photo1 = (Photo) o;
    return Objects.equals(description, photo1.description) && Objects.equals(photo, photo1.photo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), description, photo);
  }
}
