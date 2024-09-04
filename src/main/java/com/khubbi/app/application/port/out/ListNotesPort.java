package com.khubbi.app.application.port.out;

import com.khubbi.app.application.domain.model.Note;
import com.khubbi.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface ListNotesPort {

  Try<List<Note>> listNotes(User owner);
}
