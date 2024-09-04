package com.khubbi.app.application.domain.service;

import com.khubbi.app.application.domain.model.EventCreator;
import com.khubbi.app.application.domain.model.EventDate;
import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.port.in.AddNoteUseCase;
import com.khubbi.app.application.port.in.FindUserUseCase;
import com.khubbi.app.application.port.in.ListNotesUseCase;
import com.khubbi.app.application.port.out.AddNotePort;
import com.khubbi.app.application.port.out.ListNotesPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class NoteService extends UserAwareService implements AddNoteUseCase, ListNotesUseCase {

  private final AddNotePort addNotePort;
  private final ListNotesPort listNotesPort;

  NoteService(
      FindUserUseCase findUserUseCase, AddNotePort addNotePort, ListNotesPort listNotesPort) {
    super(findUserUseCase);
    this.addNotePort = addNotePort;
    this.listNotesPort = listNotesPort;
  }

  @Override
  public Try<AddNoteUseCase.Note> addNote(
      @NonNull UserEmail creatorEmail, @NonNull EventDate date, @NonNull String note) {

    log.info("Adding Note '{}' ({}) for {}", note, date, creatorEmail);

    return UserEmail.of(creatorEmail.value())
        .toTry()
        .flatMapTry(this::findUser)
        .map(user -> Note.create(note, date, EventCreator.of(user)))
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
  public Try<List<ListNotesUseCase.Note>> listNotes(@NonNull UserEmail creatorEmail) {

    log.info("Listing notes for {}", creatorEmail.value());

    return UserEmail.of(creatorEmail.value())
        .toTry()
        .flatMapTry(this::findUser)
        .flatMap(
            user ->
                listNotesPort
                    .listNotes(user)
                    .map(notes -> notes.sortBy(Note::date))
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
