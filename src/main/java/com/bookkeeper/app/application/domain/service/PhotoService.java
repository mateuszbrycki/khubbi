package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.EventCreator;
import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListPhotosUseCase;
import com.bookkeeper.app.application.port.out.AddPhotoPort;
import com.bookkeeper.app.application.port.out.ListPhotosPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.io.File;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class PhotoService implements AddPhotoUseCase, ListPhotosUseCase {

  private final FindUserUseCase findUserUseCase;
  private final AddPhotoPort addPhotoPort;
  private final ListPhotosPort listPhotosPort;

  @Override
  public Try<AddPhotoUseCase.Photo> addPhoto(
      @NonNull UserEmail creator,
      @NonNull EventDate date,
      @NonNull File photo,
      @NonNull String description) {
    log.info("Adding photo '{}' ({}) for {}", description, date, creator);

    return findUserUseCase
        .findUser(creator)
        .map(
            user ->
                com.bookkeeper.app.application.domain.model.Photo.create(
                    description, photo, date, EventCreator.of(user)))
        .flatMapTry(this.addPhotoPort::addPhoto)
        .mapTry(
            savedPhoto ->
                AddPhotoUseCase.Photo.builder()
                    .id(savedPhoto.id().value())
                    .url(this.buildURL(savedPhoto.id().value()))
                    .date(savedPhoto.date().value())
                    .build());
  }

  // TODO mateusz.brycki extract to a separate service
  private String buildURL(UUID photoId) {
    return "https://localhost:8080/photo/{}.jpg".formatted(photoId.toString());
  }

  @Override
  public Try<List<ListPhotosUseCase.Photo>> listPhotos(@NonNull UserEmail owner) {

    log.info("Listing photos for {}", owner);
    return findUserUseCase
        .findUser(owner)
        .flatMap(this.listPhotosPort::listPhotos)
        .map(photos -> photos.sortBy(com.bookkeeper.app.application.domain.model.Photo::date))
        .map(
            photos ->
                photos.map(
                    photo ->
                        ListPhotosUseCase.Photo.builder()
                            .id(photo.id().value())
                            .url(buildURL(photo.id().value()))
                            .description(photo.description())
                            .date(photo.date().value())
                            .build()));
  }
}
