package com.khubbi.app.application.domain.service;

import com.khubbi.app.application.domain.model.Event;
import com.khubbi.app.application.domain.model.Note;
import com.khubbi.app.application.domain.model.Photo;
import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.port.in.FindUserUseCase;
import com.khubbi.app.application.port.in.ListEventsUseCase;
import com.khubbi.app.application.port.out.ListEventsPort;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class EventsService extends UserAwareService implements ListEventsUseCase {

  private final ListEventsPort listEventsPort;

  public EventsService(FindUserUseCase findUserUseCase, ListEventsPort listEventsPort) {
    super(findUserUseCase);
    this.listEventsPort = listEventsPort;
  }

  @Override
  public Try<List<TimelineEvent>> listEvents(@NonNull UserEmail userEmail) {
    log.info("Listing events for {}", userEmail);

    return findUser(userEmail)
        .flatMap(listEventsPort::listEvents)
        .mapTry(
            events ->
                events
                    .map(
                        event ->
                            TimelineEvent.builder()
                                .id(event.id().value())
                                .date(event.date().value())
                                .properties(getProperties(event))
                                .build())
                    .sortBy(TimelineEvent::date));
  }

  // FIXME mateusz.brycki this method will need to be updated any time any change in model objects
  // is introduced
  private Map<String, Object> getProperties(Event event) {
    return switch (event) {
      case Photo photo ->
          HashMap.of(
              "description", photo.description(), "photo", photo.photo().id().value().toString());
      case Note note -> HashMap.of("note", note.note());
      default -> HashMap.empty();
    };
  }
}
