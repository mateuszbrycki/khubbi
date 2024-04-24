package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddUserPort;
import com.bookkeeper.app.application.port.out.ListUsersPort;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class UserInMemoryRepository implements AddUserPort, ListUsersPort {

  private Map<String, User> users;

  public UserInMemoryRepository(Map<String, User> users) {
    this.users = users;
  }

  public Try<Option<User>> findByEmail(String email) {
    return Try.of(() -> users.get(email));
  }

  // TODO mateusz.brycki welll...might be better
  public Try<User> addUser(User user) {
    return Try.of(() -> add(user));
  }

  private User add(User user) {
    this.users = this.users.put(user.getEmail(), user);
    return user;
  }

  @Override
  public Try<List<User>> listUsers() {
    return Try.of(() -> List.ofAll(users.values()));
  }
}
