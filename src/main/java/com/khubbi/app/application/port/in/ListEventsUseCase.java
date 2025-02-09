package com.khubbi.app.application.port.in;

import com.khubbi.app.application.domain.model.UserEmail;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

public interface ListEventsUseCase {

  Try<List<TimelineEvent>> listEvents(@NonNull UserEmail owner);

  @Builder(toBuilder = true)
  record TimelineEvent(
      @NonNull UUID id, @NonNull ZonedDateTime date, @NonNull Map<String, Object> properties) {}
}
