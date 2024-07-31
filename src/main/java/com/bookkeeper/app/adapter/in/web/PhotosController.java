package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListPhotosUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/photos")
public class PhotosController {

  private static final Logger LOG = LogManager.getLogger(PhotosController.class);

  private final ListPhotosUseCase listPhotosUseCase;
  private final FindUserUseCase findUserUseCase;

  public PhotosController(ListPhotosUseCase listPhotosUseCase, FindUserUseCase findUserUseCase) {
    this.listPhotosUseCase = listPhotosUseCase;
    this.findUserUseCase = findUserUseCase;
  }

  @GetMapping
  public ResponseEntity<?> listPhotos(Authentication authentication) {
    LOG.info("Received list photos request from {}", authentication.getName());

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserQuery(authentication.getName()))
        .flatMapTry(
            user -> this.listPhotosUseCase.listPhotos(new ListPhotosUseCase.ListPhotosQuery(user)))
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }
}
