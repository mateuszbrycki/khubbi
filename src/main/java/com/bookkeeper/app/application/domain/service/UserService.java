package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.in.AddUserUseCase;
import com.bookkeeper.app.application.port.out.AddUserPort;
import com.bookkeeper.app.application.port.out.ListUsersPort;
import io.vavr.collection.List;
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
    User candidate =
        new User(
            UUID.randomUUID(), command.fullName(), command.email(), command.password(), now, now);

    return this.listUsersPort
        .listUsers()
        .map(users -> users.filter(user -> user.getEmail().equals(command.email())))
        .flatMapTry(
            users ->
                users.isEmpty()
                    ? Try.success(List.empty())
                    : Try.failure(
                        new UserWithEmailExistsException("A user with given email already exists")))
        .flatMapTry(user -> this.addUserPort.addUser(candidate));
  }
}
