package com.khubbi.app.adapter.out.persistance;

import io.vavr.collection.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

  @Primary
  @Bean
  EventsInMemoryDatabase eventsInMemoryDatabase() {
    return new EventsInMemoryDatabase(HashMap.empty());
  }

  @Bean
  NoteInMemoryDatabase noteInMemoryDatabase() {
    return new NoteInMemoryDatabase(HashMap.empty());
  }

  @Bean
  PhotoInMemoryDatabase photoInMemoryDatabase() {
    return new PhotoInMemoryDatabase(HashMap.empty());
  }

  @Bean
  AttachmentsInMemoryDatabase attachmentsInMemoryDatabase() {
    return new AttachmentsInMemoryDatabase(HashMap.empty());
  }
}
