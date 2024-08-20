package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.FindAttachmentUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.out.FindAttachmentPort;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AttachmentService extends UserAwareService implements FindAttachmentUseCase {

  private final FindAttachmentPort findAttachmentPort;

  public AttachmentService(FindUserUseCase findUserUseCase, FindAttachmentPort findAttachmentPort) {
    super(findUserUseCase);
    this.findAttachmentPort = findAttachmentPort;
  }

  @Override
  public Try<Attachment> findAttachment(UserEmail userEmail, EventAttachmentId attachmentId) {
    return findUser(userEmail)
        .flatMapTry(user -> findAttachmentPort.findById(user, attachmentId))
        .mapTry(
            attachment ->
                new FindAttachmentUseCase.Attachment(attachment.id(), attachment.content()));
  }
}
