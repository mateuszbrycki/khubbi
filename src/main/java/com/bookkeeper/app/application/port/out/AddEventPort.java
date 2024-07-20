package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.Event;
import io.vavr.control.Try;

public interface AddEventPort {

  Try<Event> addEvent(Event event);
}
