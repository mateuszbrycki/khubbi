package com.bookkeeper.app.application.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Shelf {

  private final UUID id;
  private final String name;

  public Shelf(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Shelf shelf = (Shelf) o;
    return Objects.equals(id, shelf.id) && Objects.equals(name, shelf.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
