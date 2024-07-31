package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.port.in.AddNoteUseCase;
import com.bookkeeper.app.application.port.in.ListNotesUseCase;
import com.bookkeeper.app.application.port.out.AddNotePort;
import com.bookkeeper.app.application.port.out.ListNotesPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoteService implements AddNoteUseCase, ListNotesUseCase {

  private static final Logger LOG = LogManager.getLogger(NoteService.class);

  private final AddNotePort addNotePort;
  private final ListNotesPort listNotesPort;

  public NoteService(AddNotePort addNotePort, ListNotesPort listNotesPort) {
    this.addNotePort = addNotePort;
    this.listNotesPort = listNotesPort;
  }

  @Override
  public Try<AddNoteUseCase.Note> addNote(AddNoteCommand command) {

    LOG.info(
        "Adding Note '{}' ({}) for {}", command.note(), command.date(), command.owner().getId());
    com.bookkeeper.app.application.domain.model.Note candidate =
        new com.bookkeeper.app.application.domain.model.Note(
            command.note(), command.date(), command.owner());

    return this.listNotesPort
        .listNotes(command.owner())
        .flatMapTry(note -> this.addNotePort.addNote(candidate))
        .mapTry(note -> new AddNoteUseCase.Note(note.getId(), note.getNote(), note.getDate()));
  }

  @Override
  public Try<List<ListNotesUseCase.Note>> listNotes(ListNotesCommand command) {

    LOG.info("Listing notes for {}", command.owner().getId());
    return listNotesPort
        .listNotes(command.owner())
        .map(notes -> notes.sortBy(com.bookkeeper.app.application.domain.model.Note::getDate))
        .map(
            notes ->
                notes.map(
                    note ->
                        new ListNotesUseCase.Note(
                            note.getId(), note.getNote(), note.getDate())));
  }
}
