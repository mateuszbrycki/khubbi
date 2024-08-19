package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.FindAttachmentUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.out.FindAttachmentPort;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class AttachmentService implements FindAttachmentUseCase {

  private final FindUserUseCase findUserUseCase;
  private final FindAttachmentPort findAttachmentPort;

  @Override
  public Try<Attachment> findAttachment(UserEmail userEmail, EventAttachmentId attachmentId) {
    return findUserUseCase
        .findUser(userEmail)
        .flatMapTry(user -> findAttachmentPort.findById(user, attachmentId))
        .mapTry(
            attachment ->
                new FindAttachmentUseCase.Attachment(attachment.id(), attachment.content()));
  }
}
