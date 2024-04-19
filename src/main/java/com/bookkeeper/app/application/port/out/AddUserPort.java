package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;

public interface AddUserPort {

  Try<User> addUser(User user);
}
