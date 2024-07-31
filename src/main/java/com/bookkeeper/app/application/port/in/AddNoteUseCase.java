package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface AddNoteUseCase {

  Try<Note> addNote(AddNoteCommand command);

  record AddNoteCommand(String note, ZonedDateTime date, User owner) {}

  record Note(UUID id, String note, ZonedDateTime date) {}
}
