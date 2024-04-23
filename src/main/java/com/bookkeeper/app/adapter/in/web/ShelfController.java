package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.bookkeeper.app.application.domain.service.ShelfWithNameExistsException;
import com.bookkeeper.app.application.port.in.AddShelfUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shelf")
public class ShelfController {

  private final AddShelfUseCase addShelfUseCase;

  public ShelfController(AddShelfUseCase addShelfUseCase) {
    this.addShelfUseCase = addShelfUseCase;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> addShelf(@RequestBody Shelf shelf) {

    AddShelfUseCase.AddShelfCommand addShelfCommand =
        new AddShelfUseCase.AddShelfCommand(shelf.name());

    return this.addShelfUseCase
        .addShelf(addShelfCommand)
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
