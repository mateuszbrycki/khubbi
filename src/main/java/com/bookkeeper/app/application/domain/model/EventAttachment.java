package com.bookkeeper.app.application.domain.model;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class EventAttachment<T> {

  private final EventAttachmentId id;
  private final T content;

  public static class PhotoAttachment extends EventAttachment<File> {
    public PhotoAttachment(EventAttachmentId eventAttachmentId, File content) {
      super(eventAttachmentId, content);
    }

    public static PhotoAttachment create(File content) {
      return new PhotoAttachment(EventAttachmentId.random(), content);
    }
  }
}
