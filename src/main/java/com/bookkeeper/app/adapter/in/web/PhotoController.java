package com.bookkeeper.app.adapter.in.web;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

  @PostMapping(consumes = "application/x-www-form-urlencoded")
  public ResponseEntity<?> addPhoto(@ModelAttribute Photo photo, Authentication authentication) {
    LOG.info("Received add photo request {}", photo);

    return findUserUseCase
        .findUser(new FindUserUseCase.FindUserCommand(authentication.getName()))
        .mapTry(
            user ->
                new AddPhotoUseCase.AddPhotoCommand(
                    photo.payload().description(), toFile(photo), photo.date(), user))
        .flatMap(this.addPhotoUseCase::addPhoto)
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

  private static File toFile(Photo photo) throws IOException {
    String[] fileNameParts = photo.payload().photo().getOriginalFilename().split("\\.");
    String fileExtensions = fileNameParts[fileNameParts.length - 1];

    File tempFile = File.createTempFile(UUID.randomUUID().toString(), fileExtensions);
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
      IOUtils.copy(photo.payload().photo().getInputStream(), out);
    }

    return tempFile;
  }

  record Photo(Payload payload, ZonedDateTime date) {}

  record Payload(MultipartFile photo, String description) {}
}
