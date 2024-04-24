package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.in.AddUserUseCase;
import com.bookkeeper.app.application.port.out.AddUserPort;
import com.bookkeeper.app.application.port.out.ListUsersPort;
import io.vavr.control.Try;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class UserService implements AddUserUseCase {

  private final AddUserPort addUserPort;
  private final ListUsersPort listUsersPort;

  public UserService(AddUserPort addUserPort, ListUsersPort listUsersPort) {
    this.addUserPort = addUserPort;
    this.listUsersPort = listUsersPort;
  }

  @Override
  public Try<User> addUser(AddUserCommand command) {

    Date now = Date.from(Instant.now());

    // TODO mateusz.brycki check duplicates
    return addUserPort.addUser(
        new User(
            UUID.randomUUID(), command.fullName(), command.email(), command.password(), now, now));
  }
}
