package com.khubbi.app.application.domain.service;

import com.khubbi.app.application.domain.model.User;
import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.port.in.FindUserUseCase;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract class UserAwareService {

  private final FindUserUseCase findUserUseCase;

  protected Try<User> findUser(@NonNull UserEmail userEmail) {
    return findUserUseCase.findUser(userEmail);
  }
}
