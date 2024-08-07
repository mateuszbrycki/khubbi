package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.NonNull;

public interface FindUserUseCase {
  Try<User> findUser(FindUserQuery command);

  // TODO mateusz.brycki consider UserEmail here
  @Builder
  record FindUserQuery(@NonNull String email) {}
}
