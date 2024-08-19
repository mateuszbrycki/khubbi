package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.EventAttachment;
import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;

public interface FindAttachmentPort {

  Try<EventAttachment<?>> findById(User user, EventAttachmentId id);
}
