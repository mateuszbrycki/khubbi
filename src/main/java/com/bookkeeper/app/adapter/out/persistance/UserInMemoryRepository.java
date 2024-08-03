package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddUserPort;
import com.bookkeeper.app.application.port.out.ListUsersPort;
import io.vavr.Value;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserInMemoryRepository implements AddUserPort, ListUsersPort {

  private Map<String, User> users;

  public Try<User> findByEmail(String email) {
    return Try.of(() -> users.get(email)).flatMapTry(Value::toTry);
  }

  // TODO mateusz.brycki welll...might be better
  public Try<User> addUser(User user) {
    return Try.of(() -> add(user));
  }

  private User add(User user) {
    this.users = this.users.put(user.email(), user);
    return user;
  }

  @Override
  public Try<List<User>> listUsers() {
    return Try.of(() -> List.ofAll(users.values()));
  }
}
