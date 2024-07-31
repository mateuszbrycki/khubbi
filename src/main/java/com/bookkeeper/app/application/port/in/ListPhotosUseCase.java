package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface ListPhotosUseCase {

  Try<List<Photo>> listPhotos(ListPhotosQuery command);

  record ListPhotosQuery(User owner) {}

    record Photo(UUID id, String description, String url, ZonedDateTime date) {}
}
