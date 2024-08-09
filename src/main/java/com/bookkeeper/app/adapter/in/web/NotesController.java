package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.ListNotesUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
@Slf4j
@AllArgsConstructor
public class NotesController {

  private final ListNotesUseCase listNotesUseCase;

  @GetMapping
  public ResponseEntity<?> listNotes(Authentication authentication) {
    log.info("Received list notes request from {}", authentication.getName());

    return this.listNotesUseCase
        .listNotes(UserEmail.of(authentication.getName()))
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }
}
