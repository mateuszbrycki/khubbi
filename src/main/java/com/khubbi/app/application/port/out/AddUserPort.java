package com.khubbi.app.application.port.out;

import com.khubbi.app.application.domain.model.User;
import io.vavr.control.Try;

public interface AddUserPort {

  Try<User> addUser(User user);
}
