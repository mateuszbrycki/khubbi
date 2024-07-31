package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.adapter.out.persistance.NoteInMemoryDatabase;
import io.vavr.collection.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoteServiceConfiguration {

  @Bean
  NoteService noteService() {
    NoteInMemoryDatabase noteInMemoryDatabase = new NoteInMemoryDatabase(HashMap.empty());
    return new NoteService(noteInMemoryDatabase, noteInMemoryDatabase);
  }
}
