package com.khubbi.app.application.domain.service;

import com.khubbi.app.adapter.out.persistance.UserInMemoryRepository;
import com.khubbi.app.application.port.in.FindUserUseCase;
import com.khubbi.app.application.port.out.*;
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
  UserService userService(
      UserInMemoryRepository userInMemoryRepository, PasswordEncoder passwordEncoder) {
    return new UserService(userInMemoryRepository, userInMemoryRepository, passwordEncoder);
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

  @Bean
  PasswordEncoder passwordEncodingService(
      org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
    return new PasswordEncoder(passwordEncoder);
  }
}
