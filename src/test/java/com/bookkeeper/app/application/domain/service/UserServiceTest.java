package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.in.AddUserUseCase;
import com.bookkeeper.app.application.port.out.AddUserPort;
import com.bookkeeper.app.application.port.out.ListUsersPort;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private AddUserPort addUserPort;

  @Mock private ListUsersPort listUsersPort;

  @InjectMocks private UserService underTest;

  @Test
  public void shouldReturnUserWhenAddingUserSucceeded() {

    // given
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
        underTest.addUser(new AddUserUseCase.AddUserCommand("email", "password", "fullName"));

    // then
    assertTrue(user.isSuccess());
    assertEquals("email", user.get().getEmail());
    assertEquals("fullName", user.get().getFullName());
  }

  @Test
  public void shouldReturnFailureWhenAddingUserFailed() {

    // given
    when(addUserPort.addUser(any()))
        .thenReturn(Try.failure(new RuntimeException("Cannot add a user")));

    // when
    Try<User> user =
        underTest.addUser(new AddUserUseCase.AddUserCommand("email", "password", "fullName"));

    // then
    assertTrue(user.isFailure());
  }
}
