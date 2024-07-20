package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventsController {

  private static final Logger LOG = LogManager.getLogger(EventsController.class);

  private final ListEventsUseCase listEventsUseCase;
  private final FindUserUseCase findUserUseCase;

  public EventsController(ListEventsUseCase listEventsUseCase, FindUserUseCase findUserUseCase) {
    this.listEventsUseCase = listEventsUseCase;
    this.findUserUseCase = findUserUseCase;
  }

  @GetMapping
  public ResponseEntity<?> listEvents(Authentication authentication) {
    LOG.info("Received list events request from {}", authentication.getName());

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserCommand(authentication.getName()))
        .flatMapTry(
            user ->
                this.listEventsUseCase.listEvents(
                    new ListEventsUseCase.ListEventsCommand(user)))
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }
}
