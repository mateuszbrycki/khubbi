package com.bookkeeper.app.application.domain.service;

import static com.bookkeeper.app.common.Anys.*;
import static com.bookkeeper.app.common.Anys.ANY_EVENT_CREATOR;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.bookkeeper.app.application.domain.model.*;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import com.bookkeeper.app.application.port.out.ListEventsPort;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventsServiceTest {

  @Mock private ListEventsPort listEventsPort;

  @InjectMocks private EventsService underTest;

  @Test
  public void shouldReturnFailureWhenListingEventsFailed() {
    // given
    when(listEventsPort.listEvents(ANY_USER))
        .thenReturn(Try.failure(new RuntimeException("An error occurred")));

    // when
    Try<List<ListEventsUseCase.Event>> events =
        underTest.listEvents(new ListEventsUseCase.ListEventsQuery(ANY_USER));

    // then
    assertThat(events).isFailure();
    assertThat(events).failReasonHasMessage("An error occurred");
  }

  @Test
  public void shouldReturnEmptyListIfNoEventsAdded() {
    // given
    when(listEventsPort.listEvents(ANY_USER)).thenReturn(Try.success(List.empty()));

    // when
    Try<List<ListEventsUseCase.Event>> events =
        underTest.listEvents(new ListEventsUseCase.ListEventsQuery(ANY_USER));

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).isEmpty();
  }

  @Test
  public void shouldReturnEventsOrderedByDate() {
    // given
    Photo photo = new Photo("test-photo-description", ANY_FILE, ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    when(photo.photo().getAbsolutePath()).thenReturn(ANY_FILE_PATH);
    Note note =
        new Note("test-note", EventDate.of(ZonedDateTime.now().minusDays(2)), ANY_EVENT_CREATOR);

    when(listEventsPort.listEvents(ANY_USER)).thenReturn(Try.success(List.of(photo, note)));

    // when
    Try<List<ListEventsUseCase.Event>> events =
        underTest.listEvents(new ListEventsUseCase.ListEventsQuery(ANY_USER));

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).hasSize(2);
    assertThat(events.get()) // order matters!
        .containsExactly(
            new ListEventsUseCase.Event(
                note.id().value(), note.date().value(), HashMap.of("note", note.note())),
            new ListEventsUseCase.Event(
                photo.id().value(),
                photo.date().value(),
                HashMap.of(
                    "description", photo.description(), "photo", photo.photo().getAbsolutePath())));
  }

  @Test
  @Disabled("The EventsService.getProperties() method doesn't support dynamic properties mapping")
  public void shouldMapEventsPropertiesIntoMapForUnknownType() {
    // given
    TestEvent testEvent =
        new TestEvent(EventId.random(), ANY_EVENT_DATE, ANY_EVENT_CREATOR, "some-test-value");

    when(listEventsPort.listEvents(ANY_USER)).thenReturn(Try.success(List.of(testEvent)));

    // when
    Try<List<ListEventsUseCase.Event>> events =
        underTest.listEvents(new ListEventsUseCase.ListEventsQuery(ANY_USER));

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).hasSize(2);
    assertThat(events.get()) // order matters!
        .containsExactly(
            new ListEventsUseCase.Event(
                testEvent.id().value(),
                testEvent.date().value(),
                HashMap.of("testEvent", testEvent.getTestValue())));
  }

  static class TestEvent extends Event {
    private final String testValue;

    TestEvent(EventId id, EventDate date, EventCreator creator, String testValue) {
      super(id, date, creator);
      this.testValue = testValue;
    }

    public String getTestValue() {
      return testValue;
    }
  }
}
