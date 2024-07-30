package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import java.io.File;
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
@RequestMapping("/photo")
public class PhotoController {
  private static final Logger LOG = LogManager.getLogger(PhotoController.class);

  private final FindUserUseCase findUserUseCase;
  private final AddPhotoUseCase addPhotoUseCase;

  public PhotoController(FindUserUseCase findUserUseCase, AddPhotoUseCase addPhotoUseCase) {
    this.findUserUseCase = findUserUseCase;
    this.addPhotoUseCase = addPhotoUseCase;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> addEvent(@RequestBody Photo photo, Authentication authentication) {
    LOG.info("Received add photo request {}", photo);

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserCommand(authentication.getName()))
        .mapTry(
            user ->
                new AddPhotoUseCase.AddPhotoCommand(
                    photo.description(), photo.photo(), photo.date(), user))
        .flatMap(this.addPhotoUseCase::addPhoto)
        .fold(
            failure -> {
              HttpStatus status =
                  Match(failure)
                      .of(
                          Case($(instanceOf(Exception.class)), HttpStatus.INTERNAL_SERVER_ERROR));
              return new ResponseEntity<>(
                  new RequestResult.RequestError(failure.getMessage()), status);
            },
            result -> new ResponseEntity<>(result, HttpStatus.CREATED));
  }

  record Photo(String description, File photo, ZonedDateTime date) {}
}
