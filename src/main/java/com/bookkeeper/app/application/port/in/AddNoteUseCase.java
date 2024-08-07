package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.UserEmail;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface AddNoteUseCase {

  Try<Note> addNote(AddNoteCommand command);

  @Builder
  record AddNoteCommand(
      @NonNull UserEmail creator, @NonNull EventDate date, @NonNull String note) {}

  @Builder
  record Note(@NonNull UUID id, @NonNull String note, @NonNull ZonedDateTime date) {}
}
