package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.bookkeeper.app.application.domain.service.ShelfWithNameExistsException;
import com.bookkeeper.app.application.port.in.AddShelfUseCase;
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

@RestController
@RequestMapping("/shelf")
public class ShelfController {

  private static final Logger LOG = LogManager.getLogger(ShelfController.class);

  private final AddShelfUseCase addShelfUseCase;
  private final FindUserUseCase findUserUseCase;

  public ShelfController(AddShelfUseCase addShelfUseCase, FindUserUseCase findUserUseCase) {
    this.addShelfUseCase = addShelfUseCase;
    this.findUserUseCase = findUserUseCase;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> addShelf(@RequestBody Shelf shelf, Authentication authentication) {
    LOG.debug("Received add shelf request {}", shelf);

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserCommand(authentication.getName()))
        .mapTry(user -> new AddShelfUseCase.AddShelfCommand(shelf.name(), user))
        .flatMap(this.addShelfUseCase::addShelf)
        .fold(
            failure -> {
              HttpStatus status =
                  Match(failure)
                      .of(
                          Case(
                              $(instanceOf(ShelfWithNameExistsException.class)),
                              HttpStatus.CONFLICT),
                          Case($(), HttpStatus.INTERNAL_SERVER_ERROR));
              return new ResponseEntity<>(
                  new RequestResult.RequestError(failure.getMessage()), status);
            },
            result -> new ResponseEntity<>(result, HttpStatus.CREATED));
  }

  record Shelf(String name) {}
}
