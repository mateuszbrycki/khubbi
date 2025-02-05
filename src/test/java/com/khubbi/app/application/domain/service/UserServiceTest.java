package com.khubbi.app.application.domain.service;

import static com.khubbi.app.common.Anys.ANY_EMAIL;
import static com.khubbi.app.common.Anys.ANY_USER_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.khubbi.app.application.domain.model.User;
import com.khubbi.app.application.domain.model.UserPassword;
import com.khubbi.app.application.port.out.AddUserPort;
import com.khubbi.app.application.port.out.ListUsersPort;
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

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserService underTest;

  @Test
  public void shouldReturnUserAndAddsDefaultNotesWhenAddingUserSucceeded() {

    // given
    when(listUsersPort.listUsers()).thenReturn(Try.success(List.empty()));
    when(passwordEncoder.encode(any())).thenReturn("password");
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
    Try<User> user =
        underTest.addUser(
            ANY_USER_EMAIL, UserPassword.of("password").get(), UserPassword.of("password").get());

    // then
    assertTrue(user.isSuccess());
    assertEquals("email", user.get().email());
    assertEquals("fullName", user.get().fullName());
  }

  @Test
  public void shouldReturnFailureWhenAddingUserFailed() {

    // given
    when(listUsersPort.listUsers()).thenReturn(Try.success(List.empty()));
    when(passwordEncoder.encode(any())).thenReturn("password");
    when(addUserPort.addUser(any()))
        .thenReturn(Try.failure(new RuntimeException("Cannot add a user")));

    // when
    Try<User> user =
        underTest.addUser(
            ANY_USER_EMAIL, UserPassword.of("password").get(), UserPassword.of("password").get());

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
            ANY_EMAIL,
            "password",
            Date.from(Instant.now()),
            Date.from(Instant.now()));

    when(listUsersPort.listUsers()).thenReturn(Try.success(List.of(existingUser)));
    when(passwordEncoder.encode(any())).thenReturn("password");

    // when
    Try<User> user =
        underTest.addUser(
            ANY_USER_EMAIL, UserPassword.of("password").get(), UserPassword.of("password").get());

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
    Try<User> user =
        underTest.addUser(
            ANY_USER_EMAIL, UserPassword.of("password").get(), UserPassword.of("password").get());

    // then
    assertTrue(user.isFailure());
    assertInstanceOf(RuntimeException.class, user.getCause());
    assertEquals("Some random exception", user.getCause().getMessage());
  }
}
