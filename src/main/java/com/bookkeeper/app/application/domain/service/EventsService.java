package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.Note;
import com.bookkeeper.app.application.domain.model.Photo;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import com.bookkeeper.app.application.port.out.ListEventsPort;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventsService implements ListEventsUseCase {
  private static final Logger LOG = LogManager.getLogger(EventsService.class);

  private final ListEventsPort listEventsPort;

  public EventsService(ListEventsPort listEventsPort) {
    this.listEventsPort = listEventsPort;
  }

  @Override
  public Try<List<Event>> listEvents(ListEventsQuery query) {
    LOG.info("Listing events for {}", query.owner().getId());
    return this.listEventsPort
        .listEvents(query.owner())
        .mapTry(
            events ->
                events
                    .map(
                        event ->
                            new Event(
                                event.getId().value(),
                                event.getDate().value(),
                                getProperties(event)))
                    .sortBy(Event::date));
  }

  private Map<String, Object> getProperties(
      com.bookkeeper.app.application.domain.model.Event event) {
    return switch (event) {
      case Photo photo ->
          HashMap.of(
              "description", photo.getDescription(), "photo", photo.getPhoto().getAbsolutePath());
      case Note note -> HashMap.of("note", note.getNote());
      default -> HashMap.empty();
    };
  }
}
