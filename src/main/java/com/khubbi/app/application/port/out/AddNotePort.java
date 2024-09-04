package com.khubbi.app.application.port.out;

import com.khubbi.app.application.domain.model.Note;
import io.vavr.control.Try;

public interface AddNotePort {

  Try<Note> addNote(Note note);
}
