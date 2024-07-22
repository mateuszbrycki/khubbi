package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.bookkeeper.app.application.domain.service.EventWithNameExistsException;
import com.bookkeeper.app.application.port.in.AddEventUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/event")
public class EventController {

  private static final Logger LOG = LogManager.getLogger(EventController.class);

  private final AddEventUseCase addEventUseCase;
  private final FindUserUseCase findUserUseCase;

  public EventController(AddEventUseCase addEventUseCase, FindUserUseCase findUserUseCase) {
    this.addEventUseCase = addEventUseCase;
    this.findUserUseCase = findUserUseCase;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> addEvent(@RequestBody Event event, Authentication authentication) {
    LOG.info("Received add event request {}", event);

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserCommand(authentication.getName()))
        .mapTry(
            user ->
                new AddEventUseCase.AddEventCommand(
                    event.note(), LocalDateTime.parse(event.date()), user))
        .flatMap(this.addEventUseCase::addEvent)
        .fold(
            failure -> {
              HttpStatus status =
                  Match(failure)
                      .of(
                          Case(
                              $(instanceOf(EventWithNameExistsException.class)),
                              HttpStatus.CONFLICT),
                          Case($(), HttpStatus.INTERNAL_SERVER_ERROR));
              return new ResponseEntity<>(
                  new RequestResult.RequestError(failure.getMessage()), status);
            },
            result -> new ResponseEntity<>(result, HttpStatus.CREATED));
  }

  record Event(String note, String date) {}
}
