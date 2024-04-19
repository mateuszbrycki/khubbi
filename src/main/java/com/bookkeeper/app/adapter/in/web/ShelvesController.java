package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.port.in.ListShelvesUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shelves")
public class ShelvesController {

  private final ListShelvesUseCase listShelvesUseCase;

  public ShelvesController(ListShelvesUseCase listShelvesUseCase) {
    this.listShelvesUseCase = listShelvesUseCase;
  }

  @GetMapping
  public ResponseEntity<?> listShelves() {
    return this.listShelvesUseCase
        .listShelves(new ListShelvesUseCase.ListShelvesCommand())
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()), HttpStatus.CONFLICT),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }
}
