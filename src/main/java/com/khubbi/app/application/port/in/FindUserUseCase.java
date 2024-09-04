package com.khubbi.app.application.port.in;

import com.khubbi.app.application.domain.model.User;
import com.khubbi.app.application.domain.model.UserEmail;
import io.vavr.control.Try;
import lombok.NonNull;

public interface FindUserUseCase {
  Try<User> findUser(@NonNull UserEmail command);
}
