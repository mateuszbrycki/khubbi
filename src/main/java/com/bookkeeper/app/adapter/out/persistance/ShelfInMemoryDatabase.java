package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.Shelf;
import com.bookkeeper.app.application.port.out.AddShelfPort;
import com.bookkeeper.app.application.port.out.ListShelvesPort;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class ShelfInMemoryDatabase implements AddShelfPort, ListShelvesPort {

  private List<Shelf> shelves;

  public ShelfInMemoryDatabase(List<Shelf> shelves) {
    this.shelves = shelves;
  }

  @Override
  public Try<Shelf> addShelf(Shelf shelf) {
    return Try.of(() -> this.shelves = this.shelves.append(shelf)).map(t -> shelf);
  }

  @Override
  public Try<List<Shelf>> listShelves() {
    return Try.of(() -> this.shelves);
  }
}
