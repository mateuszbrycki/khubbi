package com.khubbi.app.application.port.out;

import com.khubbi.app.application.domain.model.EventAttachment;
import com.khubbi.app.application.domain.model.EventAttachmentId;
import com.khubbi.app.application.domain.model.User;
import io.vavr.control.Try;

public interface FindAttachmentPort {

  Try<EventAttachment<?>> findById(User user, EventAttachmentId id);
}
