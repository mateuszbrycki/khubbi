package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;

import com.bookkeeper.app.application.port.in.AddNoteUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/note")
@Slf4j
@AllArgsConstructor
public class NoteController {

  private final AddNoteUseCase addNoteUseCase;
  private final FindUserUseCase findUserUseCase;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> addNote(
      @RequestBody AddNoteRequest addNoteRequest, Authentication authentication) {
    log.info("Received add note request {}", addNoteRequest);

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserQuery(authentication.getName()))
        .mapTry(
            user ->
                AddNoteUseCase.AddNoteCommand.builder()
                    .note(addNoteRequest.payload().note())
                    .date(addNoteRequest.date())
                    .owner(user)
                    .build())
        .flatMap(this.addNoteUseCase::addNote)
        .fold(
            failure -> {
              HttpStatus status = Match(failure).of(Case($(), HttpStatus.INTERNAL_SERVER_ERROR));
              return new ResponseEntity<>(
                  new RequestResult.RequestError(failure.getMessage()), status);
            },
            result -> new ResponseEntity<>(result, HttpStatus.CREATED));
  }

  record Payload(@NonNull String note) {}

  record AddNoteRequest(@NonNull Payload payload, @NonNull ZonedDateTime date) {}
}
