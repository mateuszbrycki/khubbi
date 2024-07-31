package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface ListNotesUseCase {

  Try<List<Note>> listNotes(ListNotesQuery command);

  record ListNotesQuery(User owner) {}

  record Note(UUID id, String note, ZonedDateTime date) {}
}
