package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.Event;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EventInMemoryDatabaseTest {

  @Test
  public void shouldReturnEmptyListWhenNoEventsAdded() {
    // given
    EventInMemoryDatabase underTest = new EventInMemoryDatabase(HashMap.empty());

    // when
    Try<List<Event>> result = underTest.listEvents(Anys.ANY_USER);

    // then
    assertTrue(result.isSuccess());
    assertTrue(result.get().isEmpty());
  }

  @Test
  public void shouldAddNewEvent() {

    // given
    EventInMemoryDatabase underTest = new EventInMemoryDatabase(HashMap.empty());
    Event testEvent = new Event("test-event", Anys.ANY_USER);

    // when
    Try<Event> result = underTest.addEvent(testEvent);
    Try<List<Event>> events = underTest.listEvents(Anys.ANY_USER);

    // then
    assertTrue(result.isSuccess());
    assertEquals(testEvent, result.get());

    assertTrue(events.isSuccess());
    assertEquals(1, events.get().size());
    assertTrue(events.get().contains(testEvent));
  }

  @Test
  public void shouldReturnFailureWhenAddingNewEvent() {
    // given
    HashMap<User, List<Event>> events = mock(HashMap.class);
    when(events.get(any())).thenReturn(Option.none());

    EventInMemoryDatabase underTest = new EventInMemoryDatabase(events);
    Event testEvent = new Event("test-event", Anys.ANY_USER);

    Exception addingEventException = new RuntimeException("Error adding a event");
    when(events.put(eq(Anys.ANY_USER), eq(List.of(testEvent)))).thenThrow(addingEventException);

    // when
    Try<Event> result = underTest.addEvent(testEvent);

    // then
    assertTrue(result.isFailure());
    assertEquals(addingEventException, result.getCause());
  }
}
