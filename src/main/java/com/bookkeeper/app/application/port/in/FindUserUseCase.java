package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.domain.model.UserEmail;
import io.vavr.control.Try;
import lombok.NonNull;

public interface FindUserUseCase {
  Try<User> findUser(@NonNull UserEmail command);
}
