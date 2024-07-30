package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.port.in.AddEventUseCase;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import com.bookkeeper.app.application.port.out.AddEventPort;
import com.bookkeeper.app.application.port.out.ListEventsPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventService implements AddEventUseCase, ListEventsUseCase {

  private static final Logger LOG = LogManager.getLogger(EventService.class);

  private final AddEventPort addEventPort;
  private final ListEventsPort listEventsPort;

  public EventService(AddEventPort addEventPort, ListEventsPort listEventsPort) {
    this.addEventPort = addEventPort;
    this.listEventsPort = listEventsPort;
  }

  @Override
  public Try<AddEventUseCase.Event> addEvent(AddEventCommand command) {

    LOG.info(
        "Adding Event '{}' ({}) for {}", command.note(), command.date(), command.owner().getId());
    com.bookkeeper.app.application.domain.model.Event candidate =
        new com.bookkeeper.app.application.domain.model.Event(
            command.note(), command.date(), command.owner());

    return this.listEventsPort
        .listEvents(command.owner())
        .flatMapTry(event -> this.addEventPort.addEvent(candidate))
        .mapTry(event -> new AddEventUseCase.Event(event.getId(), event.getNote(), event.getDate()));
  }

  @Override
  public Try<List<ListEventsUseCase.Event>> listEvents(ListEventsCommand command) {

    LOG.info("Listing events for {}", command.owner().getId());
    return listEventsPort
        .listEvents(command.owner())
        .map(events -> events.sortBy(com.bookkeeper.app.application.domain.model.Event::getDate))
        .map(
            events ->
                events.map(
                    event ->
                        new ListEventsUseCase.Event(
                            event.getId(), event.getNote(), event.getDate())));
  }
}
