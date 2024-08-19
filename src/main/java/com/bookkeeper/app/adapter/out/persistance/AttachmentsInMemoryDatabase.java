package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.EventAttachment;
import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.domain.model.EventCreator;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddAttachmentPort;
import com.bookkeeper.app.application.port.out.FindAttachmentPort;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AttachmentsInMemoryDatabase implements AddAttachmentPort, FindAttachmentPort {

  private Map<User, List<EventAttachment<?>>> attachments;

  @Override
  public Try<EventAttachment<?>> findById(User user, EventAttachmentId id) {

    return attachments
        .getOrElse(user, List.empty())
        .filter(attachment -> attachment.id().equals(id))
        .toTry();
  }

  @Override
  public <T extends EventAttachment<?>> Try<T> addAttachment(
      EventCreator eventCreator, T attachment) {
    log.debug("Saving attachment {}", attachment.id().value().toString());

    return this.attachments
        .get(eventCreator.value())
        .orElse(Option.of(List.empty()))
        .toTry()
        .mapTry(
            attachments -> {
              this.attachments =
                  this.attachments.put(eventCreator.value(), attachments.append(attachment));
              return attachment;
            });
  }
}
