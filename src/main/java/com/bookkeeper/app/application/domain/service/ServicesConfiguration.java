package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.adapter.out.persistance.UserInMemoryRepository;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfiguration {
  @Bean
  NoteService noteService(
      FindUserUseCase findUserUseCase, AddNotePort addNotePort, ListNotesPort listNotesPort) {
    return new NoteService(findUserUseCase, addNotePort, listNotesPort);
  }

  @Bean
  PhotoService photoService(
      FindUserUseCase findUserUseCase,
      AddPhotoPort addPhotoPort,
      ListPhotosPort listPhotosPort,
      AddAttachmentPort addAttachmentPort) {
    return new PhotoService(findUserUseCase, addPhotoPort, listPhotosPort, addAttachmentPort);
  }

  @Bean
  UserService userService(UserInMemoryRepository userInMemoryRepository) {
    return new UserService(userInMemoryRepository, userInMemoryRepository);
  }

  @Bean
  EventsService eventsService(FindUserUseCase findUserUseCase, ListEventsPort listEventsPort) {
    return new EventsService(findUserUseCase, listEventsPort);
  }

  @Bean
  AttachmentService attachmentService(
      FindUserUseCase findUserUseCase, FindAttachmentPort findAttachmentPort) {
    return new AttachmentService(findUserUseCase, findAttachmentPort);
  }
}
