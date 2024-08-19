package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.EventAttachment;
import com.bookkeeper.app.application.domain.model.EventCreator;
import io.vavr.control.Try;

public interface AddAttachmentPort {
  <T extends EventAttachment<?>> Try<T> addAttachment(EventCreator eventCreator, T attachment);
}
