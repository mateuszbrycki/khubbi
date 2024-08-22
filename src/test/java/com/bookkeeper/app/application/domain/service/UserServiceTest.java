package com.bookkeeper.app.application.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddUserPort;
import com.bookkeeper.app.application.port.out.ListUsersPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private AddUserPort addUserPort;

  @Mock private ListUsersPort listUsersPort;

  @InjectMocks private UserService underTest;

  @Test
  public void shouldReturnUserAndAddsDefaultNotesWhenAddingUserSucceeded() {

    // given
    when(listUsersPort.listUsers()).thenReturn(Try.success(List.empty()));
    when(addUserPort.addUser(any()))
        .thenReturn(
            Try.success(
                new User(
                    UUID.randomUUID(),
                    "fullName",
                    "email",
                    "password",
                    Date.from(Instant.now()),
                    Date.from(Instant.now()))));

    // when
    Try<User> user = underTest.addUser("email", "password", "fullName");

    // then
    assertTrue(user.isSuccess());
    assertEquals("email", user.get().email());
    assertEquals("fullName", user.get().fullName());
  }

  @Test
  public void shouldReturnFailureWhenAddingUserFailed() {

    // given
    when(listUsersPort.listUsers()).thenReturn(Try.success(List.empty()));
    when(addUserPort.addUser(any()))
        .thenReturn(Try.failure(new RuntimeException("Cannot add a user")));

    // when
    Try<User> user = underTest.addUser("email", "password", "fullName");

    // then
    assertTrue(user.isFailure());
  }

  @Test
  public void shouldFailDueToExistingUser() {

    // given
    User existingUser =
        new User(
            UUID.randomUUID(),
            "fullName",
            "email",
            "password",
            Date.from(Instant.now()),
            Date.from(Instant.now()));

    when(listUsersPort.listUsers()).thenReturn(Try.success(List.of(existingUser)));

    // when
    Try<User> user = underTest.addUser(existingUser.email(), "password", "fullName");

    // then
    assertTrue(user.isFailure());
    assertInstanceOf(UserWithEmailExistsException.class, user.getCause());
  }

  @Test
  public void shouldFailDueToFailureWhenCheckingUserExistence() {

    // given
    when(listUsersPort.listUsers())
        .thenReturn(Try.failure(new RuntimeException("Some random exception")));

    // when
    Try<User> user = underTest.addUser("email", "password", "fullName");

    // then
    assertTrue(user.isFailure());
    assertInstanceOf(RuntimeException.class, user.getCause());
    assertEquals("Some random exception", user.getCause().getMessage());
  }
}
