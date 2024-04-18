package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.port.in.AddShelfUseCase;
import com.bookkeeper.app.application.port.in.ListShelvesUseCase;
import com.bookkeeper.app.application.port.out.AddShelfPort;
import com.bookkeeper.app.application.port.out.ListShelvesPort;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class ShelfService implements AddShelfUseCase, ListShelvesUseCase {

    private final AddShelfPort addShelfPort;
    private final ListShelvesPort listShelvesPort;

    public ShelfService(AddShelfPort addShelfPort, ListShelvesPort listShelvesPort) {
        this.addShelfPort = addShelfPort;
        this.listShelvesPort = listShelvesPort;
    }

    @Override
    public Try<AddShelfUseCase.Shelf> addShelf(AddShelfCommand command) {

        com.bookkeeper.app.application.domain.model.Shelf candidate = new com.bookkeeper.app.application.domain.model.Shelf(command.name());

        return this.listShelvesPort.listShelves()
                .map(shelves -> shelves.filter(shelf -> shelf.getName().equals(candidate.getName())))
                .flatMapTry(shelves -> shelves.isEmpty() ? Try.success(List.empty()) : Try.failure(new ShelfWithNameExistsException("A shelf with given name already exists")))
                .flatMapTry(shelf -> this.addShelfPort.addShelf(candidate))
                .mapTry(shelf -> new AddShelfUseCase.Shelf(shelf.getId(), shelf.getName()));
    }

    @Override
    public Try<List<ListShelvesUseCase.Shelf>> listShelves(ListShelvesCommand command) {
        return listShelvesPort.listShelves()
                .map(shelves -> shelves.map(shelf -> new ListShelvesUseCase.Shelf(shelf.getId(), shelf.getName())));
    }
}
