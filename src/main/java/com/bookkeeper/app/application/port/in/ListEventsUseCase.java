package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.util.UUID;

public interface ListEventsUseCase {

  Try<List<Event>> listEvents(ListEventsCommand command);

  record ListEventsCommand(User owner) {}

  record Event(UUID id, String name) {}
}
