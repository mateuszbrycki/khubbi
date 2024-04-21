package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserInMemoryRepositoryTest {

  private static final User USER =
      new User(
          UUID.randomUUID(),
          "any-fullname",
          "test-user@bookkeeper.io",
          "any-password",
          Date.from(Instant.now()),
          Date.from(Instant.now()));

  @Test
  public void shouldThrowErrorWhenAddingUser() {
    // given
    Map<String, User> users = mock(Map.class);
    UserInMemoryRepository underTest = new UserInMemoryRepository(users);

    Exception addingException = new RuntimeException("Error when adding a user");
    when(users.put(USER.getEmail(), USER)).thenThrow(addingException);

    // when
    Try<User> result = underTest.addUser(USER);

    // then
    assertTrue(result.isFailure());
    assertEquals(addingException, result.getCause());
  }

  @Test
  public void shouldThrowErrorWhenLookingForUser() {
    // given
    Map<String, User> users = mock(Map.class);
    UserInMemoryRepository underTest = new UserInMemoryRepository(users);

    Exception findingByEmailException = new RuntimeException("Error when looking for a user");
    when(users.get(USER.getEmail())).thenThrow(findingByEmailException);

    // when
    Try<Option<User>> result = underTest.findByEmail(USER.getEmail());

    // then
    assertTrue(result.isFailure());
    assertEquals(findingByEmailException, result.getCause());
  }

  @Test
  public void shouldNotFindUserIfNotAdded() {
    // given
    Map<String, User> users = HashMap.empty();
    UserInMemoryRepository underTest = new UserInMemoryRepository(users);

    // when
    Try<Option<User>> result = underTest.findByEmail(USER.getEmail());

    // then
    assertTrue(result.isSuccess());
    assertTrue(result.get().isEmpty());
  }

  @Test
  public void shouldFindUserIfAdded() {
    // given
    Map<String, User> users = HashMap.of(Tuple.of(USER.getEmail(), USER));
    UserInMemoryRepository underTest = new UserInMemoryRepository(users);

    // when
    Try<Option<User>> result = underTest.findByEmail(USER.getEmail());

    // then
    assertTrue(result.isSuccess());
    assertEquals(USER, result.get().get());
  }
}
