package com.bookkeeper.app.application.port.in;

import io.vavr.control.Try;

import java.util.UUID;

public interface AddShelfUseCase {

  Try<Shelf> addShelf(AddShelfCommand command);

  record AddShelfCommand(String name) {}

  record Shelf(UUID id, String name) {}
}
