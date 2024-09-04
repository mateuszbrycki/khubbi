package com.khubbi.app.application.port.in;

import com.khubbi.app.application.domain.model.EventAttachmentId;
import com.khubbi.app.application.domain.model.UserEmail;
import io.vavr.control.Try;
import lombok.Builder;

public interface FindAttachmentUseCase {

  Try<Attachment> findAttachment(UserEmail userEmail, EventAttachmentId attachmentId);

  @Builder
  record Attachment(EventAttachmentId attachmentId, Object content) {}
}
