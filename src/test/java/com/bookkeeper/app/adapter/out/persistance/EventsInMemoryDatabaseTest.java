package com.bookkeeper.app.adapter.out.persistance;

import static com.bookkeeper.app.common.Anys.*;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bookkeeper.app.application.domain.model.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventsInMemoryDatabaseTest {

  @Test
  public void shouldReturnEmptyListWhenNoNotesAdded() {

    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());

    // when
    Try<List<Note>> notes = underTest.listNotes(ANY_USER);

    // then
    assertThat(notes).isSuccess();
    assertThat(notes.get()).isEmpty();
  }

  @Test
  public void shouldFailWhenAddingNote() {

    // given
    HashMap<User, List<Event>> mapMock = prepareMapMock();
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(mapMock);

    Note note = Note.create("test-note", ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    // when
    Try<Note> result = underTest.addNote(note);

    // then
    assertThat(result).isFailure();
    assertThat(result).failReasonHasMessage("Some error occurred");
  }

  @Test
  public void shouldSaveNoteAndReturnOneElementList() {

    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());
    Note note = Note.create("test-note", ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    underTest.addNote(note);

    // when
    Try<List<Note>> notes = underTest.listNotes(ANY_USER);

    // then
    assertThat(notes).isSuccess();
    assertThat(notes.get()).hasSize(1);
    assertThat(notes.get()).contains(note);
  }

  @Test
  public void shouldSaveNotesAndReturnAllAddedNotes() {
    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());
    Note note1 = Note.create("test-note-1", ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    Note note2 = Note.create("test-note-2", ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    Note note3 = Note.create("test-note-3", ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    underTest.addNote(note1);
    underTest.addNote(note2);
    underTest.addNote(note3);

    // when
    Try<List<Note>> notes = underTest.listNotes(ANY_USER);

    // then
    assertThat(notes).isSuccess();
    assertThat(notes.get()).hasSize(3);
    assertThat(notes.get()).contains(note1);
    assertThat(notes.get()).contains(note2);
    assertThat(notes.get()).contains(note3);
  }

  @Test
  public void shouldFailWhenAddingPhoto() {
    // given
    HashMap<User, List<Event>> mapMock = prepareMapMock();

    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(mapMock);

    Photo photo =
        Photo.create(
            "test-photo-description", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    // when
    Try<Photo> result = underTest.addPhoto(photo);

    // then
    assertThat(result).isFailure();
    assertThat(result).failReasonHasMessage("Some error occurred");
  }

  private static HashMap<User, List<Event>> prepareMapMock() {
    HashMap mapMock = mock(HashMap.class);
    when(mapMock.get(any())).thenReturn(Option.of(List.empty()));
    when(mapMock.put(any(User.class), any(List.class)))
        .thenThrow(new RuntimeException("Some error occurred"));
    return mapMock;
  }

  @Test
  public void shouldReturnEmptyListWhenNoPhotosAdded() {

    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());

    // when
    Try<List<Photo>> notes = underTest.listPhotos(ANY_USER);

    // then
    assertThat(notes).isSuccess();
    assertThat(notes.get()).isEmpty();
  }

  @Test
  public void shouldSavePhotoAndReturnOneElementList() {
    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());
    Photo photo =
        Photo.create(
            "test-photo-description", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    underTest.addPhoto(photo);

    // when
    Try<List<Photo>> notes = underTest.listPhotos(ANY_USER);

    // then
    assertThat(notes).isSuccess();
    assertThat(notes.get()).hasSize(1);
    assertThat(notes.get()).contains(photo);
  }

  @Test
  public void shouldSavePhotosAndReturnAllAddedPhotos() {
    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());
    Photo photo1 =
        Photo.create(
            "test-photo-description-1", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    Photo photo2 =
        Photo.create(
            "test-photo-description-2", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    Photo photo3 =
        Photo.create(
            "test-photo-description-3", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    underTest.addPhoto(photo1);
    underTest.addPhoto(photo2);
    underTest.addPhoto(photo3);

    // when
    Try<List<Photo>> notes = underTest.listPhotos(ANY_USER);

    // then
    assertThat(notes).isSuccess();
    assertThat(notes.get()).hasSize(3);
    assertThat(notes.get()).contains(photo1);
    assertThat(notes.get()).contains(photo2);
    assertThat(notes.get()).contains(photo3);
  }

  @Test
  public void shouldReturnEmptyListWhenNoEventsAdded() {
    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());

    // when
    Try<List<Event>> events = underTest.listEvents(ANY_USER);

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).isEmpty();
  }

  @Test
  public void shouldReturnAllAddedEvents() {

    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());
    Photo photo =
        Photo.create(
            "test-photo-description", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    Note note = Note.create("test-note", ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    underTest.addPhoto(photo);
    underTest.addNote(note);

    // when
    Try<List<Event>> events = underTest.listEvents(ANY_USER);

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).hasSize(2);
    assertThat(events.get()).contains(photo);
    assertThat(events.get()).contains(note);
  }

  @Test
  public void shouldReturnEventsCreatedByGivenUser() {
    // given
    EventsInMemoryDatabase underTest = new EventsInMemoryDatabase(HashMap.empty());
    Photo photo1 =
        Photo.create(
            "test-photo-description", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    Note note1 = Note.create("test-note", ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    Photo photo2 =
        Photo.create(
            "test-photo-description",
            ANY_PHOTO_ATTACHMENT,
            ANY_EVENT_DATE,
            ANY_ANOTHER_EVENT_CREATOR);
    Note note2 = Note.create("test-note", ANY_EVENT_DATE, ANY_ANOTHER_EVENT_CREATOR);

    underTest.addPhoto(photo1);
    underTest.addNote(note1);
    underTest.addPhoto(photo2);
    underTest.addNote(note2);

    // when
    Try<List<Event>> events = underTest.listEvents(ANY_USER);

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).hasSize(2);
    assertThat(events.get()).contains(photo1);
    assertThat(events.get()).contains(note1);

    // when
    events = underTest.listEvents(ANY_ANOTHER_USER);

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).hasSize(2);
    assertThat(events.get()).contains(photo2);
    assertThat(events.get()).contains(note2);
  }
}
