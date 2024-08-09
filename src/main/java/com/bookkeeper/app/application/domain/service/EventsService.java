package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.Note;
import com.bookkeeper.app.application.domain.model.Photo;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import com.bookkeeper.app.application.port.out.ListEventsPort;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class EventsService implements ListEventsUseCase {

  private final FindUserUseCase findUserUseCase;
  private final ListEventsPort listEventsPort;

  @Override
  public Try<List<Event>> listEvents(UserEmail email) {
    log.info("Listing events for {}", email);

    return this.findUserUseCase
        .findUser(email)
        .flatMap(listEventsPort::listEvents)
        .mapTry(
            events ->
                events
                    .map(
                        event ->
                            Event.builder()
                                .id(event.id().value())
                                .date(event.date().value())
                                .properties(getProperties(event))
                                .build())
                    .sortBy(Event::date));
  }

  private Map<String, Object> getProperties(
      com.bookkeeper.app.application.domain.model.Event event) {
    return switch (event) {
      case Photo photo ->
          HashMap.of("description", photo.description(), "photo", photo.photo().getAbsolutePath());
      case Note note -> HashMap.of("note", note.note());
      default -> HashMap.empty();
    };
  }
}
