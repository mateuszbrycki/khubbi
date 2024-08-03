package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface ListPhotosUseCase {

  Try<List<Photo>> listPhotos(ListPhotosQuery command);

  @Builder
  record ListPhotosQuery(@NonNull User owner) {}

  @Builder
  record Photo(
      @NonNull UUID id,
      @NonNull String description,
      @NonNull String url,
      @NonNull ZonedDateTime date) {}
}
