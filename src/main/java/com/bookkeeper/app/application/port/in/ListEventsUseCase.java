package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface ListEventsUseCase {

  Try<List<Event>> listEvents(ListEventsQuery query);

  @Builder
  record ListEventsQuery(@NonNull User owner) {}

  @Builder
  record Event(
      @NonNull UUID id, @NonNull ZonedDateTime date, @NonNull Map<String, Object> properties) {}
}
