package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.NonNull;

public interface AddUserUseCase {

  Try<User> addUser(AddUserCommand command);

  @Builder
  record AddUserCommand(
      @NonNull String email, @NonNull String password, @NonNull String fullName) {}
}
