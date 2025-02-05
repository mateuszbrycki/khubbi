package com.khubbi.app.adapter.in.web;

import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.port.in.ListPhotosUseCase;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
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

    return UserEmail.of(authentication.getName())
        .toTry()
        .flatMapTry(this.listPhotosUseCase::listPhotos)
        .map(
            events ->
                events.map(
                    event ->
                        PhotoResponse.builder()
                            .id(event.id())
                            .description(event.description())
                            .url("http://localhost:8080/attachment/" + event.attachmentId())
                            .date(event.date())
                            .build()))
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestStringError(failure.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR),
            result -> new ResponseEntity<>(result.toJavaList(), HttpStatus.OK));
  }

  @Builder
  record PhotoResponse(
      @NonNull UUID id,
      @NonNull String description,
      @NonNull String url,
      @NonNull ZonedDateTime date) {}
}
