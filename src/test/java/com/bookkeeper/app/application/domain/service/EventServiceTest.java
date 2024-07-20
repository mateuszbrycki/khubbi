package com.bookkeeper.app.application.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bookkeeper.app.application.domain.model.Event;
import com.bookkeeper.app.application.port.in.AddEventUseCase;
import com.bookkeeper.app.application.port.out.AddEventPort;
import com.bookkeeper.app.application.port.out.ListEventsPort;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.List;
import io.vavr.control.Try;
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

    when(listEventsPort.listEvents(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addEventPort.addEvent(any()))
        .thenReturn(Try.success(new Event("new-event", Anys.ANY_USER)));

    Try<AddEventUseCase.Event> result =
        this.underTest.addEvent(new AddEventUseCase.AddEventCommand("new-event", Anys.ANY_USER));

    assertTrue(result.isSuccess());
    assertEquals("new-event", result.get().name());
  }

  @Test
  public void testAddingEventFailedDueToNameOverlap() {

    when(listEventsPort.listEvents(Anys.ANY_USER))
        .thenReturn(
            Try.success(
                List.of(
                    new Event("old-event", Anys.ANY_USER), new Event("new-event", Anys.ANY_USER))));

    Try<AddEventUseCase.Event> result =
        this.underTest.addEvent(new AddEventUseCase.AddEventCommand("new-event", Anys.ANY_USER));

    assertTrue(result.isFailure());
    assertInstanceOf(EventWithNameExistsException.class, result.getCause());
  }

  @Test
  public void testAddingEventFailedDueToExceptionWhenAddingTheEvent() {

    when(listEventsPort.listEvents(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addEventPort.addEvent(any())).thenThrow(new RuntimeException("Any random failure"));

    Try<AddEventUseCase.Event> result =
        this.underTest.addEvent(new AddEventUseCase.AddEventCommand("new-event", Anys.ANY_USER));

    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void testAddingDefaultEventsWithOneError() {

    when(listEventsPort.listEvents(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addEventPort.addEvent(any()))
        .thenReturn(Try.success(new Event("New", Anys.ANY_USER)))
        .thenThrow(new RuntimeException("Any random failure"));

    Try<List<AddEventUseCase.Event>> result =
        this.underTest.addDefaultEvents(
            new AddEventUseCase.AddDefaultEventsCommand(Anys.ANY_USER));

    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void testAddingDefaultEventsWithoutAnyError() {

    when(listEventsPort.listEvents(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addEventPort.addEvent(any()))
        .thenReturn(Try.success(new Event("New", Anys.ANY_USER)))
        .thenReturn(Try.success(new Event("Scanned", Anys.ANY_USER)));

    Try<List<AddEventUseCase.Event>> result =
        this.underTest.addDefaultEvents(
            new AddEventUseCase.AddDefaultEventsCommand(Anys.ANY_USER));

    assertTrue(result.isSuccess());
    assertEquals(2, result.get().size());
    verify(addEventPort, times(2)).addEvent(any());
  }
}
