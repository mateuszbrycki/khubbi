package com.bookkeeper.app.application.port.out;

import com.bookkeeper.app.application.domain.model.Photo;
import io.vavr.control.Try;

public interface AddPhotoPort {

    Try<Photo> addPhoto(Photo photo);
}
