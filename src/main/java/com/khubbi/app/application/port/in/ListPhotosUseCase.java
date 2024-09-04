package com.khubbi.app.application.port.in;

import com.khubbi.app.application.domain.model.UserEmail;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface ListPhotosUseCase {

  Try<List<Photo>> listPhotos(@NonNull UserEmail owner);

  @Builder(toBuilder = true)
  record Photo(
      @NonNull UUID id,
      @NonNull String description,
      @NonNull UUID attachmentId,
      @NonNull ZonedDateTime date) {}
}
