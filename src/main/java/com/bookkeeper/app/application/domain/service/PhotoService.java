package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.in.ListPhotosUseCase;
import com.bookkeeper.app.application.port.out.AddPhotoPort;
import com.bookkeeper.app.application.port.out.ListPhotosPort;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhotoService implements AddPhotoUseCase, ListPhotosUseCase {
  private static final Logger LOG = LogManager.getLogger(PhotoService.class);

  private final AddPhotoPort addPhotoPort;
  private final ListPhotosPort listPhotosPort;

  public PhotoService(AddPhotoPort addPhotoPort, ListPhotosPort listPhotosPort) {
    this.addPhotoPort = addPhotoPort;
    this.listPhotosPort = listPhotosPort;
  }

  @Override
  public Try<AddPhotoUseCase.Photo> addPhoto(AddPhotoCommand command) {
    LOG.info(
        "Adding photo '{}' ({}) for {}",
        command.description(),
        command.date(),
        command.owner().getId());
    com.bookkeeper.app.application.domain.model.Photo candidate =
        new com.bookkeeper.app.application.domain.model.Photo(
            command.description(), command.photo(), command.date(), command.owner());
    return this.addPhotoPort
        .addPhoto(candidate)
        .mapTry(
            photo ->
                new AddPhotoUseCase.Photo(
                    photo.getId(), this.buildURL(photo.getId()), photo.getDate()));
  }

  // TODO mateusz.brycki extract to a separate service
  private String buildURL(UUID photoId) {
    return "https://localhost:8080/photo/{}.jpg";
  }

  @Override
  public Try<List<ListPhotosUseCase.Photo>> listPhotos(ListPhotosCommand command) {

    LOG.info("Listing photos for {}", command.owner().getId());
    return listPhotosPort
        .listPhotos(command.owner())
        .map(photos -> photos.sortBy(com.bookkeeper.app.application.domain.model.Photo::getDate))
        .map(
            photos ->
                photos.map(
                    photo ->
                        new ListPhotosUseCase.Photo(
                            photo.getId(),
                            photo.getDescription(),
                            buildURL(photo.getId()),
                            photo.getDate())));
  }
}
