package com.bookkeeper.app.adapter.in.memory;

import com.bookkeeper.app.application.port.in.AddUserUseCase;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAutoRegistration {
    private final AddUserUseCase addUserUseCase;
    private final PasswordEncoder passwordEncoder;


    public UserAutoRegistration(AddUserUseCase addUserUseCase, PasswordEncoder passwordEncoder) {
        this.addUserUseCase = addUserUseCase;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    void registerUser() {

        addUserUseCase.addUser(new AddUserUseCase.AddUserCommand("test1@test.com", passwordEncoder.encode("test1"), "Test 1"))
                .getOrElseThrow(() -> new RuntimeException("Cannot Register user Test 1"));
        addUserUseCase.addUser(new AddUserUseCase.AddUserCommand("test2@test.com", passwordEncoder.encode("test2"), "Test 2"))
                .getOrElseThrow(() -> new RuntimeException("Cannot Register user Test 2"));
    }
}
