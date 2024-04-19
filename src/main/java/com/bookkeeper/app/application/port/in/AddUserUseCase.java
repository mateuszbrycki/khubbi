package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;

public interface AddUserUseCase {

  Try<User> addUser(AddUserCommand command);

  record AddUserCommand(String email, String password, String fullName) {}
}
