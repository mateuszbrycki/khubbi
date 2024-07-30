package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.Event;
import com.bookkeeper.app.application.domain.model.Photo;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.out.AddPhotoPort;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class PhotoInMemoryDatabase implements AddPhotoPort {
  private Map<User, List<Photo>> photos;

  public PhotoInMemoryDatabase(Map<User, List<Photo>> photos) {
    this.photos = photos;
  }

  @Override
  public Try<Photo> addPhoto(Photo photo) {

    return this.photos
        .get(photo.getOwner())
        .orElse(Option.of(List.empty()))
        .toTry()
        .mapTry(
            events -> {
              this.photos = this.photos.put(photo.getOwner(), events.append(photo));
              return this.photos;
            })
        .mapTry(t -> photo);
  }

  public Try<List<Photo>> listPhotos(User owner) {
    return this.photos.get(owner).orElse(Option.of(List.empty())).toTry();
  }
}
