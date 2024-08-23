package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@Slf4j
@AllArgsConstructor
public class EventsController {

  private final ListEventsUseCase listEventsUseCase;

  @GetMapping
  public ResponseEntity<?> listNotes(Authentication authentication) {
    log.info("Received list events request from {}", authentication.getName());

    return UserEmail.of(authentication.getName())
        .toTry()
        .flatMapTry(this.listEventsUseCase::listEvents)
        .map(
            events ->
                events.map(
                    event ->
                        event.toBuilder()
                            .properties(
                                event.properties().get("photo").isDefined()
                                    ? event
                                        .properties()
                                        .put(
                                            "photo",
                                            "http://localhost:8080/attachment/"
                                                + event.properties().get("photo").get())
                                    : event.properties())
                            .build()))
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }
}
