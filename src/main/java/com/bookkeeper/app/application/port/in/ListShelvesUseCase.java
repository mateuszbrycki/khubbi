package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.UUID;

public interface ListShelvesUseCase {

  Try<List<Shelf>> listShelves(ListShelvesCommand command);

  record ListShelvesCommand(User owner) {}

  record Shelf(UUID id, String name) {}
}
