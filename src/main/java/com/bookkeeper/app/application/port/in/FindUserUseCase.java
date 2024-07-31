package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;

public interface FindUserUseCase {
  Try<User> findUser(FindUserQuery command);

  record FindUserQuery(String email) {}
}
