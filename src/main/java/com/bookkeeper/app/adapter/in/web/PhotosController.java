package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.ListPhotosUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/photos")
@Slf4j
@AllArgsConstructor
public class PhotosController {

  private final ListPhotosUseCase listPhotosUseCase;

  @GetMapping
  public ResponseEntity<?> listPhotos(Authentication authentication) {
    log.info("Received list photos request from {}", authentication.getName());

    return this.listPhotosUseCase
        .listPhotos(UserEmail.of(authentication.getName()))
        .map(
            events ->
                events.map(
                    event ->
                        event.toBuilder()
                            .url("http://localhost:8080/attachment/" + event.url())
                            .build()))
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }
}
