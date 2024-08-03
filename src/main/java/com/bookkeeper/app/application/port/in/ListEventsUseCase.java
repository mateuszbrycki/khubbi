package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface ListEventsUseCase {

  Try<List<Event>> listEvents(ListEventsQuery query);

  record ListEventsQuery(User owner) {}

  record Event(UUID id, ZonedDateTime date, Map<String, Object> properties) {}
}
