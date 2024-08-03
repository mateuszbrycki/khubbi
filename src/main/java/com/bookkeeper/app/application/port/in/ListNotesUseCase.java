package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface ListNotesUseCase {

  Try<List<Note>> listNotes(ListNotesQuery command);

  @Builder
  record ListNotesQuery(@NonNull User owner) {}

  @Builder
  record Note(@NonNull UUID id, @NonNull String note, @NonNull ZonedDateTime date) {}
}
