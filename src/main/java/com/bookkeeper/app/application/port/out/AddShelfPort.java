package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.Shelf;
import io.vavr.control.Try;

public interface AddShelfPort {

    Try<Shelf> addShelf(Shelf shelf);
}
