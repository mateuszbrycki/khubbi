package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.port.in.AddEventUseCase;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import com.bookkeeper.app.application.port.out.AddEventPort;
import com.bookkeeper.app.application.port.out.ListEventsPort;
import io.vavr.Value;
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

    LOG.info("Adding Event '{}' for {}", command.name(), command.owner().getId());
    com.bookkeeper.app.application.domain.model.Event candidate =
        new com.bookkeeper.app.application.domain.model.Event(command.name(), command.owner());

    return this.listEventsPort
        .listEvents(command.owner())
        .map(events -> events.filter(event -> event.getName().equals(candidate.getName())))
        .flatMapTry(
            events ->
                events.isEmpty()
                    ? Try.success(List.empty())
                    : Try.failure(
                        new EventWithNameExistsException("A event with given name already exists")))
        .flatMapTry(event -> this.addEventPort.addEvent(candidate))
        .mapTry(event -> new AddEventUseCase.Event(event.getId(), event.getName()));
  }

  @Override
  public Try<List<AddEventUseCase.Event>> addDefaultEvents(AddDefaultEventsCommand command) {
    LOG.info("Adding default events for {}", command.owner().getId());

    return Try.sequence(
            List.of("New", "Scanned")
                .map(name -> this.addEvent(new AddEventCommand(name, command.owner()))))
        .mapTry(Value::toList);
  }

  @Override
  public Try<List<ListEventsUseCase.Event>> listEvents(ListEventsCommand command) {

    LOG.info("Listing events for {}", command.owner().getId());
    return listEventsPort
        .listEvents(command.owner())
        .map(
            events ->
                events.map(event -> new ListEventsUseCase.Event(event.getId(), event.getName())));
  }
}
