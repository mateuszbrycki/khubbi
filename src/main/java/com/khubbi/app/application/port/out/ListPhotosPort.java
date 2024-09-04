package com.khubbi.app.application.port.out;

import com.khubbi.app.application.domain.model.Photo;
import com.khubbi.app.application.domain.model.User;
import io.vavr.collection.List;
import io.vavr.control.Try;

public interface ListPhotosPort {
    Try<List<Photo>> listPhotos(User owner);
}
