package com.bookkeeper.app.application.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Event {

  private final UUID id;
  private final String name;
  private final User owner;

  public Event(String name, User owner) {
    this.owner = owner;
    this.id = UUID.randomUUID();
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public User getOwner() {
    return owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return Objects.equals(id, event.id)
        && Objects.equals(name, event.name)
        && Objects.equals(owner, event.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, owner);
  }
}
