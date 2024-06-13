package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.UUID;

public interface AddShelfUseCase {

  Try<Shelf> addShelf(AddShelfCommand command);

  record AddShelfCommand(String name, User owner) {}

  Try<List<Shelf>> addDefaultShelves(AddDefaultShelvesCommand command);

  record AddDefaultShelvesCommand(User owner) {}

  record Shelf(UUID id, String name) {}
}
