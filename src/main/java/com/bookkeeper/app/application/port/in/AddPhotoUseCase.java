package com.bookkeeper.app.application.port.in;

import com.bookkeeper.app.application.domain.model.User;
import io.vavr.control.Try;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.UUID;

public interface AddPhotoUseCase {

    Try<Photo> addPhoto(AddPhotoCommand command);

    record AddPhotoCommand(String description, File photo, ZonedDateTime date, User owner) {}

    record Photo(UUID id, String url, ZonedDateTime date) {}
}
