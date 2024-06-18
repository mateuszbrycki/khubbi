package com.bookkeeper.app.adapter.out.persistance;

import io.vavr.collection.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

  @Bean
  UserInMemoryRepository inMemoryUserRepository() {
    return new UserInMemoryRepository(HashMap.empty());
  }

  @Bean
  UserTokenRepository userTokenRepository() {
    return new UserTokenRepository(HashMap.empty());
  }

  @Bean
  RefreshTokenRepository refreshTokenRepository() {
    return new RefreshTokenRepository(HashMap.empty());
  }
}
