package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AddEventUseCase {

  Try<Event> addEvent(AddEventCommand command);

  record AddEventCommand(String note, LocalDateTime date, User owner) {}

  record Event(UUID id, String note, LocalDateTime date) {}
}
