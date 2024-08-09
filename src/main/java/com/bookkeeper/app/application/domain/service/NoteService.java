package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.EventCreator;
import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.AddNoteUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListNotesUseCase;
import com.bookkeeper.app.application.port.out.AddNotePort;
import com.bookkeeper.app.application.port.out.ListNotesPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class NoteService implements AddNoteUseCase, ListNotesUseCase {

  private final FindUserUseCase findUserUseCase;
  private final AddNotePort addNotePort;
  private final ListNotesPort listNotesPort;

  @Override
  public Try<AddNoteUseCase.Note> addNote(
      @NonNull UserEmail creator, @NonNull EventDate date, @NonNull String note) {

    log.info("Adding Note '{}' ({}) for {}", note, date, creator);

    return findUserUseCase
        .findUser(UserEmail.of(creator.value()))
        .map(
            user ->
                new com.bookkeeper.app.application.domain.model.Note(
                    note, date, EventCreator.of(user)))
        .flatMapTry(this.addNotePort::addNote)
        .mapTry(
            savedDone ->
                AddNoteUseCase.Note.builder()
                    .id(savedDone.id().value())
                    .note(savedDone.note())
                    .date(savedDone.date().value())
                    .build());
  }

  @Override
  public Try<List<ListNotesUseCase.Note>> listNotes(@NonNull UserEmail creator) {

    log.info("Listing notes for {}", creator.value());

    return findUserUseCase
        .findUser(UserEmail.of(creator.value()))
        .flatMap(
            user ->
                listNotesPort
                    .listNotes(user)
                    .map(
                        notes ->
                            notes.sortBy(com.bookkeeper.app.application.domain.model.Note::date))
                    .map(
                        notes ->
                            notes.map(
                                note ->
                                    ListNotesUseCase.Note.builder()
                                        .id(note.id().value())
                                        .note(note.note())
                                        .date(note.date().value())
                                        .build())));
  }
}
