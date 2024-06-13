package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.port.in.AddShelfUseCase;
import com.bookkeeper.app.application.port.in.ListShelvesUseCase;
import com.bookkeeper.app.application.port.out.AddShelfPort;
import com.bookkeeper.app.application.port.out.ListShelvesPort;
import io.vavr.Value;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShelfService implements AddShelfUseCase, ListShelvesUseCase {

  private static final Logger LOG = LogManager.getLogger(ShelfService.class);

  private final AddShelfPort addShelfPort;
  private final ListShelvesPort listShelvesPort;

  public ShelfService(AddShelfPort addShelfPort, ListShelvesPort listShelvesPort) {
    this.addShelfPort = addShelfPort;
    this.listShelvesPort = listShelvesPort;
  }

  @Override
  public Try<AddShelfUseCase.Shelf> addShelf(AddShelfCommand command) {

    LOG.debug("Adding Shelf '{}' for {}", command.name(), command.owner().getId());
    com.bookkeeper.app.application.domain.model.Shelf candidate =
        new com.bookkeeper.app.application.domain.model.Shelf(command.name(), command.owner());

    return this.listShelvesPort
        .listShelves(command.owner())
        .map(shelves -> shelves.filter(shelf -> shelf.getName().equals(candidate.getName())))
        .flatMapTry(
            shelves ->
                shelves.isEmpty()
                    ? Try.success(List.empty())
                    : Try.failure(
                        new ShelfWithNameExistsException("A shelf with given name already exists")))
        .flatMapTry(shelf -> this.addShelfPort.addShelf(candidate))
        .mapTry(shelf -> new AddShelfUseCase.Shelf(shelf.getId(), shelf.getName()));
  }

  @Override
  public Try<List<AddShelfUseCase.Shelf>> addDefaultShelves(AddDefaultShelvesCommand command) {
    LOG.debug("Adding default shelves for {}", command.owner().getId());

    return Try.sequence(
            List.of("New", "Scanned")
                .map(name -> this.addShelf(new AddShelfCommand(name, command.owner()))))
        .mapTry(Value::toList);
  }

  @Override
  public Try<List<ListShelvesUseCase.Shelf>> listShelves(ListShelvesCommand command) {

    LOG.debug("Listing shelves for {}", command.owner().getId());
    return listShelvesPort
        .listShelves(command.owner())
        .map(
            shelves ->
                shelves.map(shelf -> new ListShelvesUseCase.Shelf(shelf.getId(), shelf.getName())));
  }
}
