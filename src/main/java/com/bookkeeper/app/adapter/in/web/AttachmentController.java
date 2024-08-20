package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.FindAttachmentUseCase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachment")
@Slf4j
@AllArgsConstructor
public class AttachmentController {

  private final FindAttachmentUseCase findAttachmentUseCase;

  @GetMapping("/{id}")
  public ResponseEntity<?> listAttachment(
      Authentication authentication, @PathVariable("id") String id) {
    log.info("Received list events request from {}", authentication.getName());

    return this.findAttachmentUseCase
        .findAttachment(
            UserEmail.of(authentication.getName()), EventAttachmentId.of(UUID.fromString(id)))
        .map(
            attachment -> {
              try {
                // FIXME mateusz.brycki this cast might not always be true, especially that
                // EventAttachment is parameterized. Moreover, it might not always be a file.
                return Files.readAllBytes(((File) attachment.content()).toPath());
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
        .fold(
            failure ->
                new ResponseEntity<>(
                    new RequestResult.RequestError(failure.getMessage()), HttpStatus.NOT_FOUND),
            result -> ResponseEntity.ok().body(result));
  }
}
