package com.khubbi.app.adapter.in.memory;

import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.domain.model.UserPassword;
import com.khubbi.app.application.port.in.AddUserUseCase;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserAutoRegistration {
  private final AddUserUseCase addUserUseCase;
  private final PasswordEncoder passwordEncoder;

  @PostConstruct
  void registerUser() {

    addUserUseCase
        .addUser(
            UserEmail.of("test1@test.com").get(),
            UserPassword.of("test1").get(),
            UserPassword.of("test1").get())
        .getOrElseThrow(() -> new RuntimeException("Cannot Register user Test 1"));
    addUserUseCase
        .addUser(
            UserEmail.of("test2@test.com").get(),
            UserPassword.of("test2").get(),
            UserPassword.of("test2").get())
        .getOrElseThrow(() -> new RuntimeException("Cannot Register user Test 2"));
  }
}
