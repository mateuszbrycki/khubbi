package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.Shelf;
import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface ListShelvesPort {

  Try<List<Shelf>> listShelves(User owner);
}
