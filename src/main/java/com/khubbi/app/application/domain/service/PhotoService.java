package com.khubbi.app.application.domain.service;

import com.khubbi.app.application.domain.model.EventAttachment;
import com.khubbi.app.application.domain.model.EventCreator;
import com.khubbi.app.application.domain.model.EventDate;
import com.khubbi.app.application.domain.model.UserEmail;
import com.khubbi.app.application.port.in.AddPhotoUseCase;
import com.khubbi.app.application.port.in.FindUserUseCase;
import com.khubbi.app.application.port.in.ListPhotosUseCase;
import com.khubbi.app.application.port.out.AddAttachmentPort;
import com.khubbi.app.application.port.out.AddPhotoPort;
import com.khubbi.app.application.port.out.ListPhotosPort;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.io.File;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PhotoService extends UserAwareService implements AddPhotoUseCase, ListPhotosUseCase {

  private final AddPhotoPort addPhotoPort;
  private final ListPhotosPort listPhotosPort;
  private final AddAttachmentPort addAttachmentPort;

  PhotoService(
      FindUserUseCase findUserUseCase,
      AddPhotoPort addPhotoPort,
      ListPhotosPort listPhotosPort,
      AddAttachmentPort addAttachmentPort) {
    super(findUserUseCase);
    this.addPhotoPort = addPhotoPort;
    this.listPhotosPort = listPhotosPort;
    this.addAttachmentPort = addAttachmentPort;
  }

  @Override
  public Try<AddPhotoUseCase.Photo> addPhoto(
      @NonNull UserEmail creatorEmail,
      @NonNull EventDate date,
      @NonNull File photo,
      @NonNull String description) {

    log.info("Adding photo '{}' ({}) for {}", description, date, creatorEmail);

    return findUser(creatorEmail)
        .flatMapTry(
            user -> {
              EventCreator eventCreator = EventCreator.of(user);
              EventAttachment.PhotoAttachment eventAttachment =
                  EventAttachment.PhotoAttachment.create(photo);
              return addAttachmentPort
                  .addAttachment(eventCreator, eventAttachment)
                  .map(savedAttachment -> Tuple.of(eventCreator, savedAttachment));
            })
        .map(
            eventCreatorAndAttachment ->
                Photo.create(
                    description, eventCreatorAndAttachment._2,
                    date, eventCreatorAndAttachment._1))
        .flatMapTry(this.addPhotoPort::addPhoto)
        .mapTry(
            savedPhoto ->
                AddPhotoUseCase.Photo.builder()
                    .id(savedPhoto.id().value())
                    .attachmentId(savedPhoto.photo().id().value())
                    .date(savedPhoto.date().value())
                    .build());
  }

  @Override
  public Try<List<ListPhotosUseCase.Photo>> listPhotos(@NonNull UserEmail ownerEmail) {

    log.info("Listing photos for {}", ownerEmail);

    return findUser(ownerEmail)
        .flatMap(this.listPhotosPort::listPhotos)
        .map(photos -> photos.sortBy(Photo::date))
        .map(
            photos ->
                photos.map(
                    photo ->
                        ListPhotosUseCase.Photo.builder()
                            .id(photo.id().value())
                            .attachmentId(photo.photo().id().value())
                            .description(photo.description())
                            .date(photo.date().value())
                            .build()));
  }
}
