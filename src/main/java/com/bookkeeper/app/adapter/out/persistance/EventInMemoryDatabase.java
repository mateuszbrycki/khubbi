package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.Event;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddEventPort;
import com.bookkeeper.app.application.port.out.ListEventsPort;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class EventInMemoryDatabase implements AddEventPort, ListEventsPort {

  private Map<User, List<Event>> events;

  public EventInMemoryDatabase(Map<User, List<Event>> events) {
    this.events = events;
  }

  @Override
  public Try<Event> addEvent(Event event) {

    return this.events
        .get(event.getOwner())
        .orElse(Option.of(List.empty()))
        .toTry()
        .mapTry(
            events -> {
              this.events = this.events.put(event.getOwner(), events.append(event));
              return this.events;
            })
        .mapTry(t -> event);
  }

  @Override
  public Try<List<Event>> listEvents(User owner) {
    return this.events.get(owner).orElse(Option.of(List.empty())).toTry();
  }
}
