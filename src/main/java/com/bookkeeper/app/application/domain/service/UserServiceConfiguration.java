package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.adapter.out.persistance.UserInMemoryRepository;
import com.bookkeeper.app.application.port.in.AddShelfUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {

  @Bean
  UserService userService(
      UserInMemoryRepository userInMemoryRepository, AddShelfUseCase addShelfUseCase) {
    return new UserService(userInMemoryRepository, userInMemoryRepository, addShelfUseCase);
  }
}
