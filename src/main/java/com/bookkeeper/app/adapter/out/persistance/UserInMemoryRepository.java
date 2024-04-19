package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddUserPort;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.HashMap;
import java.util.Map;

public class UserInMemoryRepository implements AddUserPort {

  private final Map<String, User> users = new HashMap<>();

  public Option<User> findByEmail(String email) {

    return Try.of(() -> Option.of(users.get(email))).getOrElse(Option::none);
  }

  // TODO mateusz.brycki welll...might be better
  public Try<User> addUser(User user) {
    return Try.of(() -> add(user));
  }

  private User add(User user) {
    this.users.put(user.getEmail(), user);
    return user;
  }
}
