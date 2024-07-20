package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.adapter.out.persistance.EventInMemoryDatabase;
import io.vavr.collection.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventServiceConfiguration {

  @Bean
  EventService eventService() {
    EventInMemoryDatabase eventInMemoryDatabase = new EventInMemoryDatabase(HashMap.empty());
    return new EventService(eventInMemoryDatabase, eventInMemoryDatabase);
  }
}
