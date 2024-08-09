package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.domain.model.UserEmail;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface ListUsersPort {
  Try<List<User>> listUsers();

  Try<User> findByEmail(UserEmail email);
}
