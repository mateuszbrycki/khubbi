package com.khubbi.app.application.port.out;

import com.khubbi.app.application.domain.model.Photo;
import io.vavr.control.Try;

public interface AddPhotoPort {

    Try<Photo> addPhoto(Photo photo);
}
