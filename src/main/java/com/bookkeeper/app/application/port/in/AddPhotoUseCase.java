package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.UserEmail;
import io.vavr.control.Try;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface AddPhotoUseCase {

  Try<Photo> addPhoto(
      @NonNull UserEmail creator,
      @NonNull EventDate date,
      @NonNull File photo,
      @NonNull String description);

  @Builder
  record Photo(@NonNull UUID id, @NonNull String url, @NonNull ZonedDateTime date) {}
}
