package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.domain.model.UserEmail;
import io.vavr.control.Try;
import lombok.Builder;

public interface FindAttachmentUseCase {

  Try<Attachment> findAttachment(UserEmail userEmail, EventAttachmentId attachmentId);

  @Builder
  record Attachment(EventAttachmentId attachmentId, Object content) {}
}
