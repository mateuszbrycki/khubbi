package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.port.in.AddShelfUseCase;
import com.bookkeeper.app.common.RequestResult;
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

  @PostMapping
  public ResponseEntity<?> addShelf(@RequestBody Shelf shelf) {

    AddShelfUseCase.AddShelfCommand addShelfCommand =
        new AddShelfUseCase.AddShelfCommand(shelf.name());

    return this.addShelfUseCase
        .addShelf(addShelfCommand)
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()), HttpStatus.CONFLICT),
            result -> new ResponseEntity<>(result, HttpStatus.CREATED));
  }

  record Shelf(String name) {}
}
