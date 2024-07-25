package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface AddEventUseCase {

  Try<Event> addEvent(AddEventCommand command);

  record AddEventCommand(String note, ZonedDateTime date, User owner) {}

  record Event(UUID id, String note, ZonedDateTime date) {}
}
