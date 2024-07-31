package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.bookkeeper.app.application.port.in.AddNoteUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import java.time.ZonedDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/note")
public class NoteController {

  private static final Logger LOG = LogManager.getLogger(NoteController.class);

  private final AddNoteUseCase addNoteUseCase;
  private final FindUserUseCase findUserUseCase;

  public NoteController(AddNoteUseCase addNoteUseCase, FindUserUseCase findUserUseCase) {
    this.addNoteUseCase = addNoteUseCase;
    this.findUserUseCase = findUserUseCase;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> addNote(
      @RequestBody AddNoteRequest addNoteRequest, Authentication authentication) {
    LOG.info("Received add note request {}", addNoteRequest);

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserCommand(authentication.getName()))
        .mapTry(
            user ->
                new AddNoteUseCase.AddNoteCommand(
                    addNoteRequest.payload().note(), addNoteRequest.date(), user))
        .flatMap(this.addNoteUseCase::addNote)
        .fold(
            failure -> {
              HttpStatus status = Match(failure).of(Case($(), HttpStatus.INTERNAL_SERVER_ERROR));
              return new ResponseEntity<>(
                  new RequestResult.RequestError(failure.getMessage()), status);
            },
            result -> new ResponseEntity<>(result, HttpStatus.CREATED));
  }

  record Payload(String note) {}

  record AddNoteRequest(Payload payload, ZonedDateTime date) {}
}
