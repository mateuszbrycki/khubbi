package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.AddUserUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.out.AddUserPort;
import com.bookkeeper.app.application.port.out.ListUsersPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class UserService implements AddUserUseCase, FindUserUseCase {

  private final AddUserPort addUserPort;
  private final ListUsersPort listUsersPort;

  @Override
  public Try<User> addUser(AddUserCommand command) {

    Date now = Date.from(Instant.now());
    User candidate =
        new User(
            UUID.randomUUID(), command.fullName(), command.email(), command.password(), now, now);

    log.info("Adding user {} ({} - {})", candidate.id(), candidate.email(), candidate.fullName());
    return this.listUsersPort
        .listUsers()
        .map(users -> users.filter(user -> user.email().equals(command.email())))
        .flatMapTry(
            users ->
                users.isEmpty()
                    ? Try.success(List.empty())
                    : Try.failure(
                        new UserWithEmailExistsException("A user with given email already exists")))
        .flatMapTry(user -> this.addUserPort.addUser(candidate));
  }

  @Override
  public Try<User> findUser(@NonNull UserEmail userEmail) {
    log.info("Looking for user {}", userEmail);

    return this.listUsersPort.findByEmail(userEmail).toTry();
  }
}
