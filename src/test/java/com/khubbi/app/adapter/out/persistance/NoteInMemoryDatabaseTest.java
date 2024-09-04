package com.khubbi.app.adapter.out.persistance;

import static com.khubbi.app.common.Anys.ANY_EVENT_CREATOR;
import static com.khubbi.app.common.Anys.ANY_EVENT_DATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.khubbi.app.application.domain.model.Note;
import com.khubbi.app.application.domain.model.User;
import com.khubbi.app.common.Anys;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

class NoteInMemoryDatabaseTest {

  @Test
  public void shouldReturnEmptyListWhenNoNotesAdded() {
    // given
    NoteInMemoryDatabase underTest = new NoteInMemoryDatabase(HashMap.empty());

    // when
    Try<List<Note>> result = underTest.listNotes(Anys.ANY_USER);

    // then
    assertTrue(result.isSuccess());
    assertTrue(result.get().isEmpty());
  }

  @Test
  public void shouldAddNewNote() {

    // given
    NoteInMemoryDatabase underTest = new NoteInMemoryDatabase(HashMap.empty());
    Note testNote = Note.create("test-note", ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    // when
    Try<Note> result = underTest.addNote(testNote);
    Try<List<Note>> notes = underTest.listNotes(Anys.ANY_USER);

    // then
    assertTrue(result.isSuccess());
    assertEquals(testNote, result.get());

    assertTrue(notes.isSuccess());
    assertEquals(1, notes.get().size());
    assertTrue(notes.get().contains(testNote));
  }

  @Test
  public void shouldReturnFailureWhenAddingNewNote() {
    // given
    HashMap<User, List<Note>> notes = mock(HashMap.class);
    when(notes.get(any())).thenReturn(Option.none());

    NoteInMemoryDatabase underTest = new NoteInMemoryDatabase(notes);
    Note testNote = Note.create("test-note", ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    Exception addingNoteException = new RuntimeException("Error adding a note");
    when(notes.put(eq(Anys.ANY_USER), eq(List.of(testNote)))).thenThrow(addingNoteException);

    // when
    Try<Note> result = underTest.addNote(testNote);

    // then
    assertTrue(result.isFailure());
    assertEquals(addingNoteException, result.getCause());
  }
}
