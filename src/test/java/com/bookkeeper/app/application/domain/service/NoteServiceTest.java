package com.bookkeeper.app.application.domain.service;

import static com.bookkeeper.app.common.Anys.*;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.Note;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.in.AddNoteUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListNotesUseCase;
import com.bookkeeper.app.application.port.out.AddNotePort;
import com.bookkeeper.app.application.port.out.ListNotesPort;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
  @Mock private FindUserUseCase findUserUseCase;

  @Mock private AddNotePort addNotePort;

  @Mock private ListNotesPort listNotesPort;

  @InjectMocks private NoteService underTest;

  @Test
  public void testNoteSuccessfullyAdded() {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(ANY_USER));
    when(addNotePort.addNote(any()))
        .thenReturn(Try.success(Note.create("new-note", ANY_EVENT_DATE, ANY_EVENT_CREATOR)));

    // when
    Try<AddNoteUseCase.Note> result =
        this.underTest.addNote(ANY_USER_EMAIL, EventDate.now(), "new-note");

    // then
    assertTrue(result.isSuccess());
    assertEquals("new-note", result.get().note());
  }

  @Test
  public void shouldReturnExceptionWhenRetrievingUserFailedWhenAddingNote() {

    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new RuntimeException("Cannot Find User")));

    // when
    Try<AddNoteUseCase.Note> result =
        this.underTest.addNote(ANY_USER_EMAIL, EventDate.now(), "new-note");

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void shouldReturnExceptionWhenUserNotFoundWhenAddingNote() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Option.<User>none().toTry());

    // when
    Try<AddNoteUseCase.Note> result =
        this.underTest.addNote(ANY_USER_EMAIL, EventDate.now(), "new-note");

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void testAddingNoteFailedDueToExceptionWhenAddingTheNote() {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(ANY_USER));
    when(addNotePort.addNote(any())).thenThrow(new RuntimeException("Any random failure"));

    // when
    Try<AddNoteUseCase.Note> result =
        this.underTest.addNote(ANY_USER_EMAIL, EventDate.now(), "new-note");

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void shouldReturnExceptionWhenRetrievingUserFailedWhenListingNotes() {

    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new RuntimeException("Cannot Find User")));

    // when
    Try<List<ListNotesUseCase.Note>> result = this.underTest.listNotes(ANY_USER_EMAIL);

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void shouldReturnExceptionWhenUserNotFoundWhenListingNotes() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Option.<User>none().toTry());

    // when
    Try<List<ListNotesUseCase.Note>> result = this.underTest.listNotes(ANY_USER_EMAIL);

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void testListNotesSortedByDate() {

    // given
    EventDate firstDate =
        EventDate.of(LocalDateTime.parse("2009-12-03T10:15:30").atZone(ZoneId.systemDefault()));
    EventDate secondDate =
        EventDate.of(LocalDateTime.parse("2024-12-04T10:16:30").atZone(ZoneId.systemDefault()));

    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(listNotesPort.listNotes(Anys.ANY_USER))
        .thenReturn(
            Try.success(
                List.of(
                    Note.create("first-note", firstDate, ANY_EVENT_CREATOR),
                    Note.create("second-note", secondDate, ANY_EVENT_CREATOR))));

    // when
    Try<List<ListNotesUseCase.Note>> result = this.underTest.listNotes(ANY_USER_EMAIL);

    // then
    assertTrue(result.isSuccess());
    assertEquals(2, result.get().size());
    assertEquals(firstDate.value(), result.get().get(0).date());
    assertEquals(secondDate.value(), result.get().get(1).date());
  }
}
