package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListShelvesUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shelves")
public class ShelvesController {

  private static final Logger LOG = LogManager.getLogger(ShelvesController.class);

  private final ListShelvesUseCase listShelvesUseCase;
  private final FindUserUseCase findUserUseCase;

  public ShelvesController(ListShelvesUseCase listShelvesUseCase, FindUserUseCase findUserUseCase) {
    this.listShelvesUseCase = listShelvesUseCase;
    this.findUserUseCase = findUserUseCase;
  }

  @GetMapping
  public ResponseEntity<?> listShelves(Authentication authentication) {
    LOG.info("Received list shelf request from {}", authentication.getName());

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserCommand(authentication.getName()))
        .flatMapTry(
            user ->
                this.listShelvesUseCase.listShelves(
                    new ListShelvesUseCase.ListShelvesCommand(user)))
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }
}
