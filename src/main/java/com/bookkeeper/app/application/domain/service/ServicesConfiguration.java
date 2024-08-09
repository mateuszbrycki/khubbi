package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.adapter.out.persistance.UserInMemoryRepository;
import com.bookkeeper.app.application.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfiguration {
  @Bean
  NoteService noteService(
      UserService userService, AddNotePort addNotePort, ListNotesPort listNotesPort) {
    return new NoteService(userService, addNotePort, listNotesPort);
  }

  @Bean
  PhotoService photoService(
      UserService userService, AddPhotoPort addPhotoPort, ListPhotosPort listPhotosPort) {
    return new PhotoService(userService, addPhotoPort, listPhotosPort);
  }

  @Bean
  UserService userService(UserInMemoryRepository userInMemoryRepository) {
    return new UserService(userInMemoryRepository, userInMemoryRepository);
  }

  @Bean
  EventsService eventsService(ListEventsPort listEventsPort) {
    return new EventsService(listEventsPort);
  }
}
