package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.Note;
import io.vavr.control.Try;

public interface AddNotePort {

  Try<Note> addNote(Note note);
}
