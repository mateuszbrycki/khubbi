package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.Shelf;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddShelfPort;
import com.bookkeeper.app.application.port.out.ListShelvesPort;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class ShelfInMemoryDatabase implements AddShelfPort, ListShelvesPort {

  private Map<User, List<Shelf>> shelves;

  public ShelfInMemoryDatabase(Map<User, List<Shelf>> shelves) {
    this.shelves = shelves;
  }

  @Override
  public Try<Shelf> addShelf(Shelf shelf) {

    return this.shelves
        .get(shelf.getOwner())
        .orElse(Option.of(List.empty()))
        .toTry()
        .mapTry(
            shelves -> {
              this.shelves = this.shelves.put(shelf.getOwner(), shelves.append(shelf));
              return this.shelves;
            })
        .mapTry(t -> shelf);
  }

  @Override
  public Try<List<Shelf>> listShelves(User owner) {
    return this.shelves.get(owner).orElse(Option.of(List.empty())).toTry();
  }
}
