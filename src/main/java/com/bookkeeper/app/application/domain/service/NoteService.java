package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.EventCreator;
import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.port.in.AddNoteUseCase;
import com.bookkeeper.app.application.port.in.ListNotesUseCase;
import com.bookkeeper.app.application.port.out.AddNotePort;
import com.bookkeeper.app.application.port.out.ListNotesPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class NoteService implements AddNoteUseCase, ListNotesUseCase {

  private final AddNotePort addNotePort;
  private final ListNotesPort listNotesPort;

  @Override
  public Try<AddNoteUseCase.Note> addNote(AddNoteCommand command) {

    log.info("Adding Note '{}' ({}) for {}", command.note(), command.date(), command.owner().id());
    com.bookkeeper.app.application.domain.model.Note candidate =
        new com.bookkeeper.app.application.domain.model.Note(
            command.note(), EventDate.of(command.date()), EventCreator.of(command.owner()));

    return this.listNotesPort
        .listNotes(command.owner())
        .flatMapTry(note -> this.addNotePort.addNote(candidate))
        .mapTry(
            note ->
                AddNoteUseCase.Note.builder()
                    .id(note.id().value())
                    .note(note.note())
                    .date(note.date().value())
                    .build());
  }

  @Override
  public Try<List<ListNotesUseCase.Note>> listNotes(ListNotesQuery command) {

    log.info("Listing notes for {}", command.owner().id());
    return listNotesPort
        .listNotes(command.owner())
        .map(notes -> notes.sortBy(com.bookkeeper.app.application.domain.model.Note::date))
        .map(
            notes ->
                notes.map(
                    note ->
                        ListNotesUseCase.Note.builder()
                            .id(note.id().value())
                            .note(note.note())
                            .date(note.date().value())
                            .build()));
  }
}
