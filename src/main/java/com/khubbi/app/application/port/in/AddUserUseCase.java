package com.khubbi.app.application.port.in;

import com.khubbi.app.application.domain.model.User;
import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.domain.model.UserPassword;
import io.vavr.control.Try;
import lombok.NonNull;

public interface AddUserUseCase {

  Try<User> addUser(
      @NonNull UserEmail email,
      @NonNull UserPassword password,
      @NonNull UserPassword repeatedPassword);
}
