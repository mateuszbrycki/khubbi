package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.adapter.out.persistance.InMemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {

    @Bean
    UserService userService(InMemoryUserRepository inMemoryUserRepository) {
        return new UserService(inMemoryUserRepository);
    }
}
