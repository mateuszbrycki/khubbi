package com.khubbi.app.application.port.in;

import com.khubbi.app.application.domain.model.UserEmail;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface ListNotesUseCase {

  Try<List<Note>> listNotes(@NonNull UserEmail creator);

  @Builder
  record Note(@NonNull UUID id, @NonNull String note, @NonNull ZonedDateTime date) {}
}
