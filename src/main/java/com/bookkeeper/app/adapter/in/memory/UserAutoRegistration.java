package com.bookkeeper.app.adapter.in.memory;

import com.bookkeeper.app.application.port.in.AddUserUseCase;
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
        .addUser("test1@test.com", passwordEncoder.encode("test1"), "Test 1")
        .getOrElseThrow(() -> new RuntimeException("Cannot Register user Test 1"));
    addUserUseCase
        .addUser("test2@test.com", passwordEncoder.encode("test2"), "Test 2")
        .getOrElseThrow(() -> new RuntimeException("Cannot Register user Test 2"));
  }
}
