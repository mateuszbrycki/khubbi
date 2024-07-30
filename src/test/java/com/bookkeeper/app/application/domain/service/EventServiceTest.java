package com.bookkeeper.app.application.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bookkeeper.app.application.domain.model.Event;
import com.bookkeeper.app.application.port.in.AddEventUseCase;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import com.bookkeeper.app.application.port.out.AddEventPort;
import com.bookkeeper.app.application.port.out.ListEventsPort;
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
class EventServiceTest {
  @Mock private AddEventPort addEventPort;

  @Mock private ListEventsPort listEventsPort;

  @InjectMocks private EventService underTest;

  @Test
  public void testEventSuccessfullyAdded() {

    // given
    when(listEventsPort.listEvents(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addEventPort.addEvent(any()))
        .thenReturn(Try.success(new Event("new-event", ZonedDateTime.now(), Anys.ANY_USER)));

    // when
    Try<AddEventUseCase.Event> result =
        this.underTest.addEvent(
            new AddEventUseCase.AddEventCommand("new-event", ZonedDateTime.now(), Anys.ANY_USER));

    // then
    assertTrue(result.isSuccess());
    assertEquals("new-event", result.get().note());
  }

  @Test
  public void testAddingEventFailedDueToExceptionWhenAddingTheEvent() {

    // given
    when(listEventsPort.listEvents(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addEventPort.addEvent(any())).thenThrow(new RuntimeException("Any random failure"));

    // when
    Try<AddEventUseCase.Event> result =
        this.underTest.addEvent(
            new AddEventUseCase.AddEventCommand("new-event", ZonedDateTime.now(), Anys.ANY_USER));

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void testListEventsSortedByDate() {

    // given
    ZonedDateTime firstDate =
        LocalDateTime.parse("2009-12-03T10:15:30").atZone(ZoneId.systemDefault());
    ZonedDateTime secondDate =
        LocalDateTime.parse("2024-12-04T10:16:30").atZone(ZoneId.systemDefault());

    when(listEventsPort.listEvents(Anys.ANY_USER))
        .thenReturn(
            Try.success(
                List.of(
                    new Event("first-event", firstDate, Anys.ANY_USER),
                    new Event( d"second-event", secondDate, Anys.ANY_USER))));

    // when
    Try<List<ListEventsUseCase.Event>> result =
        this.underTest.listEvents(new ListEventsUseCase.ListEventsCommand(Anys.ANY_USER));

    // then
    assertTrue(result.isSuccess());
    assertEquals(2, result.get().size());
    assertEquals(firstDate, result.get().get(0).date());
    assertEquals(secondDate, result.get().get(1).date());
  }
}
