package com.bookkeeper.app.application.domain.service;

import static com.bookkeeper.app.common.Anys.ANY_EVENT_CREATOR;
import static com.bookkeeper.app.common.Anys.ANY_EVENT_DATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.Note;
import com.bookkeeper.app.application.port.in.AddNoteUseCase;
import com.bookkeeper.app.application.port.in.ListNotesUseCase;
import com.bookkeeper.app.application.port.out.AddNotePort;
import com.bookkeeper.app.application.port.out.ListNotesPort;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
  @Mock private AddNotePort addNotePort;

  @Mock private ListNotesPort listNotesPort;

  @InjectMocks private NoteService underTest;

  @Test
  public void testNoteSuccessfullyAdded() {

    // given
    when(listNotesPort.listNotes(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addNotePort.addNote(any()))
        .thenReturn(Try.success(new Note("new-note", ANY_EVENT_DATE, ANY_EVENT_CREATOR)));

    // when
    Try<AddNoteUseCase.Note> result =
        this.underTest.addNote(
            new AddNoteUseCase.AddNoteCommand("new-note", ZonedDateTime.now(), Anys.ANY_USER));

    // then
    assertTrue(result.isSuccess());
    assertEquals("new-note", result.get().note());
  }

  @Test
  public void testAddingNoteFailedDueToExceptionWhenAddingTheNote() {

    // given
    when(listNotesPort.listNotes(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addNotePort.addNote(any())).thenThrow(new RuntimeException("Any random failure"));

    // when
    Try<AddNoteUseCase.Note> result =
        this.underTest.addNote(
            new AddNoteUseCase.AddNoteCommand("new-note", ZonedDateTime.now(), Anys.ANY_USER));

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void testListNotesSortedByDate() {

    // given
    EventDate firstDate =
        EventDate.of(LocalDateTime.parse("2009-12-03T10:15:30").atZone(ZoneId.systemDefault()));
    EventDate secondDate =
        EventDate.of(LocalDateTime.parse("2024-12-04T10:16:30").atZone(ZoneId.systemDefault()));

    when(listNotesPort.listNotes(Anys.ANY_USER))
        .thenReturn(
            Try.success(
                List.of(
                    new Note("first-note", firstDate, ANY_EVENT_CREATOR),
                    new Note("second-note", secondDate, ANY_EVENT_CREATOR))));

    // when
    Try<List<ListNotesUseCase.Note>> result =
        this.underTest.listNotes(new ListNotesUseCase.ListNotesQuery(Anys.ANY_USER));

    // then
    assertTrue(result.isSuccess());
    assertEquals(2, result.get().size());
    assertEquals(firstDate.value(), result.get().get(0).date());
    assertEquals(secondDate.value(), result.get().get(1).date());
  }
}
