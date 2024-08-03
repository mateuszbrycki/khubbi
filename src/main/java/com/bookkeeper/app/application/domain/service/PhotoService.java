package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.EventCreator;
import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.in.ListPhotosUseCase;
import com.bookkeeper.app.application.port.out.AddPhotoPort;
import com.bookkeeper.app.application.port.out.ListPhotosPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
class PhotoService implements AddPhotoUseCase, ListPhotosUseCase {

  private final AddPhotoPort addPhotoPort;
  private final ListPhotosPort listPhotosPort;

  @Override
  public Try<AddPhotoUseCase.Photo> addPhoto(AddPhotoCommand command) {
    log.info(
        "Adding photo '{}' ({}) for {}",
        command.description(),
        command.date(),
        command.owner().id());
    com.bookkeeper.app.application.domain.model.Photo candidate =
        new com.bookkeeper.app.application.domain.model.Photo(
            command.description(),
            command.photo(),
            EventDate.of(command.date()),
            EventCreator.of(command.owner()));
    return this.addPhotoPort
        .addPhoto(candidate)
        .mapTry(
            photo ->
                AddPhotoUseCase.Photo.builder()
                    .id(photo.id().value())
                    .url(this.buildURL(photo.id().value()))
                    .date(photo.date().value())
                    .build());
  }

  // TODO mateusz.brycki extract to a separate service
  private String buildURL(UUID photoId) {
    return "https://localhost:8080/photo/{}.jpg".formatted(photoId.toString());
  }

  @Override
  public Try<List<ListPhotosUseCase.Photo>> listPhotos(ListPhotosQuery command) {

    log.info("Listing photos for {}", command.owner().id());
    return listPhotosPort
        .listPhotos(command.owner())
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
