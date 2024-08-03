package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.Event;
import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface ListEventsPort {
  Try<List<Event>> listEvents(User owner);
}
