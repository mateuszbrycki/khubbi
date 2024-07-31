package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListNotesUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
public class NotesController {

  private static final Logger LOG = LogManager.getLogger(NotesController.class);

  private final ListNotesUseCase listNotesUseCase;
  private final FindUserUseCase findUserUseCase;

  public NotesController(ListNotesUseCase listNotesUseCase, FindUserUseCase findUserUseCase) {
    this.listNotesUseCase = listNotesUseCase;
    this.findUserUseCase = findUserUseCase;
  }

  @GetMapping
  public ResponseEntity<?> listNotes(Authentication authentication) {
    LOG.info("Received list notes request from {}", authentication.getName());

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserCommand(authentication.getName()))
        .flatMapTry(
            user ->
                this.listNotesUseCase.listNotes(
                    new ListNotesUseCase.ListNotesCommand(user)))
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }
}
