package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.Note;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddNotePort;
import com.bookkeeper.app.application.port.out.ListNotesPort;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NoteInMemoryDatabase implements AddNotePort, ListNotesPort {

  private Map<User, List<Note>> notes;

  @Override
  public Try<Note> addNote(Note note) {

    return this.notes
        .get(note.creator().value())
        .orElse(Option.of(List.empty()))
        .toTry()
        .mapTry(
            notes -> {
              this.notes = this.notes.put(note.creator().value(), notes.append(note));
              return this.notes;
            })
        .mapTry(t -> note);
  }

  @Override
  public Try<List<Note>> listNotes(User owner) {
    return this.notes.get(owner).orElse(Option.of(List.empty())).toTry();
  }
}
