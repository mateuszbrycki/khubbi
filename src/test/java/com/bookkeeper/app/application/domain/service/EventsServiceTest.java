package com.bookkeeper.app.application.domain.service;

import static com.bookkeeper.app.common.Anys.*;
import static com.bookkeeper.app.common.Anys.ANY_EVENT_CREATOR;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.bookkeeper.app.application.domain.model.*;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import com.bookkeeper.app.application.port.out.ListEventsPort;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
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

  @Mock private FindUserUseCase findUserUseCase;
  @Mock private ListEventsPort listEventsPort;

  @InjectMocks private EventsService underTest;

  @Test
  public void shouldReturnExceptionWhenRetrievingUserFailedWhenListingEvents() {
    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new RuntimeException("User not found")));
    // when
    Try<List<ListEventsUseCase.TimelineEvent>> events = underTest.listEvents(ANY_USER_EMAIL);

    // then
    assertThat(events).isFailure();
    assertThat(events).failReasonHasMessage("User not found");
  }

  @Test
  public void shouldReturnExceptionWhenUserNotFound() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Option.<User>none().toTry());

    // when
    Try<List<ListEventsUseCase.TimelineEvent>> result = underTest.listEvents(ANY_USER_EMAIL);

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void shouldReturnFailureWhenListingEventsFailed() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(ANY_USER));
    when(listEventsPort.listEvents(ANY_USER))
        .thenReturn(Try.failure(new RuntimeException("An error occurred")));

    // when
    Try<List<ListEventsUseCase.TimelineEvent>> events = underTest.listEvents(ANY_USER_EMAIL);

    // then
    assertThat(events).isFailure();
    assertThat(events).failReasonHasMessage("An error occurred");
  }

  @Test
  public void shouldReturnEmptyListIfNoEventsAdded() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(ANY_USER));
    when(listEventsPort.listEvents(ANY_USER)).thenReturn(Try.success(List.empty()));

    // when
    Try<List<ListEventsUseCase.TimelineEvent>> events = underTest.listEvents(ANY_USER_EMAIL);

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).isEmpty();
  }

  @Test
  public void shouldReturnEventsOrderedByDate() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(ANY_USER));

    Photo photo =
        Photo.create(
            "test-photo-description", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    Note note =
        Note.create("test-note", EventDate.of(ZonedDateTime.now().minusDays(2)), ANY_EVENT_CREATOR);

    when(listEventsPort.listEvents(ANY_USER)).thenReturn(Try.success(List.of(photo, note)));

    // when
    Try<List<ListEventsUseCase.TimelineEvent>> events = underTest.listEvents(ANY_USER_EMAIL);

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).hasSize(2);
    assertThat(events.get()) // order matters!
        .containsExactly(
            new ListEventsUseCase.TimelineEvent(
                note.id().value(), note.date().value(), HashMap.of("note", note.note())),
            new ListEventsUseCase.TimelineEvent(
                photo.id().value(),
                photo.date().value(),
                HashMap.of(
                    "description",
                    photo.description(),
                    "photo",
                    photo.photo().id().value().toString())));
  }

  @Test
  @Disabled("The EventsService.getProperties() method doesn't support dynamic properties mapping")
  public void shouldMapEventsPropertiesIntoMapForUnknownType() {
    // given
    TestEvent testEvent =
        new TestEvent(EventId.random(), ANY_EVENT_DATE, ANY_EVENT_CREATOR, "some-test-value");

    when(listEventsPort.listEvents(ANY_USER)).thenReturn(Try.success(List.of(testEvent)));

    // when
    Try<List<ListEventsUseCase.TimelineEvent>> events = underTest.listEvents(ANY_USER_EMAIL);

    // then
    assertThat(events).isSuccess();
    assertThat(events.get()).hasSize(2);
    assertThat(events.get()) // order matters!
        .containsExactly(
            new ListEventsUseCase.TimelineEvent(
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
