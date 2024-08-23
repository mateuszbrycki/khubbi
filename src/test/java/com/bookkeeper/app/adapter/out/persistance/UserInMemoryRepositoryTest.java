package com.bookkeeper.app.adapter.out.persistance;

import static com.bookkeeper.app.common.Anys.ANY_USER;
import static com.bookkeeper.app.common.Anys.ANY_USER_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class UserInMemoryRepositoryTest {
  

  @Test
  public void shouldThrowErrorWhenAddingUser() {
    // given
    Map<String, User> users = mock(Map.class);
    UserInMemoryRepository underTest = new UserInMemoryRepository(users);

    Exception addingException = new RuntimeException("Error when adding a user");
    when(users.put(ANY_USER.email(), ANY_USER)).thenThrow(addingException);

    // when
    Try<User> result = underTest.addUser(ANY_USER);

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
    when(users.get(ANY_USER.email())).thenThrow(findingByEmailException);

    // when
    Try<User> result = underTest.findByEmail(ANY_USER_EMAIL);

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
    Try<User> result = underTest.findByEmail(ANY_USER_EMAIL);

    // then
    assertTrue(result.isFailure());
    assertEquals(result.getCause().getClass(), NoSuchElementException.class);
  }

  @Test
  public void shouldFindUserIfAdded() {
    // given
    Map<String, User> users = HashMap.of(Tuple.of(ANY_USER.email(), ANY_USER));
    UserInMemoryRepository underTest = new UserInMemoryRepository(users);

    // when
    Try<User> result = underTest.findByEmail(ANY_USER_EMAIL);

    // then
    assertTrue(result.isSuccess());
    assertEquals(ANY_USER, result.get());
  }
}
