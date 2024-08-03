package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface AddPhotoUseCase {

  Try<Photo> addPhoto(AddPhotoCommand command);

  @Builder
  record AddPhotoCommand(
      @NonNull String description,
      @NonNull File photo,
      @NonNull ZonedDateTime date,
      @NonNull User owner) {}

  @Builder
  record Photo(@NonNull UUID id, @NonNull String url, @NonNull ZonedDateTime date) {}
}
