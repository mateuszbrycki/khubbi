package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import io.vavr.API;
import io.vavr.control.Validation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photo")
@Slf4j
@AllArgsConstructor
public class PhotoController {

  private final AddPhotoUseCase addPhotoUseCase;

  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addPhoto(
      @ModelAttribute AddPhotoRequest addPhotoRequest, Authentication authentication)
      throws IOException {
    log.info("Received add photo request {}", addPhotoRequest);
    return Validation.combine(
            UserEmail.of(authentication.getName()), EventDate.of(addPhotoRequest.date()))
        .ap(API::Tuple)
        .toTry()
        .flatMapTry(
            userEmailAndEventDate ->
                userEmailAndEventDate
                    .append(toFile(addPhotoRequest))
                    .append(addPhotoRequest.payload().description())
                    .apply(this.addPhotoUseCase::addPhoto))
        .fold(
            failure -> {
              HttpStatus status =
                  Match(failure)
                      .of(Case($(instanceOf(Exception.class)), HttpStatus.INTERNAL_SERVER_ERROR));
              return new ResponseEntity<>(
                  new RequestResult.RequestError(failure.getMessage()), status);
            },
            result -> new ResponseEntity<>(result, HttpStatus.CREATED));
  }

  private static File toFile(AddPhotoRequest addPhotoRequest) throws IOException {
    String[] fileNameParts = addPhotoRequest.payload().photo().getOriginalFilename().split("\\.");
    String fileExtensions = fileNameParts[fileNameParts.length - 1];

    File tempFile = File.createTempFile(UUID.randomUUID().toString(), fileExtensions);
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
      IOUtils.copy(addPhotoRequest.payload().photo().getInputStream(), out);
    }

    return tempFile;
  }

  record AddPhotoRequest(@NonNull Payload payload, @NonNull ZonedDateTime date) {}

  record Payload(@NonNull MultipartFile photo, @NonNull String description) {}
}
