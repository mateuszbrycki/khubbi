package com.bookkeeper.app.adapter.out.persistance;

import static com.bookkeeper.app.common.Anys.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bookkeeper.app.application.domain.model.Photo;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

public class PhotoInMemoryDatabaseTest {

  @Test
  public void shouldReturnEmptyListWhenNoPhotosAdded() {
    // given
    PhotoInMemoryDatabase underTest = new PhotoInMemoryDatabase(HashMap.empty());

    // when
    Try<List<Photo>> result = underTest.listPhotos(Anys.ANY_USER);

    // then
    assertTrue(result.isSuccess());
    assertTrue(result.get().isEmpty());
  }

  @Test
  public void shouldAddNewPhoto() {

    // given
    PhotoInMemoryDatabase underTest = new PhotoInMemoryDatabase(HashMap.empty());
    Photo testPhoto = new Photo("test-photo", ANY_FILE, ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    // when
    Try<Photo> result = underTest.addPhoto(testPhoto);
    Try<List<Photo>> photos = underTest.listPhotos(Anys.ANY_USER);

    // then
    assertTrue(result.isSuccess());
    assertEquals(testPhoto, result.get());

    assertTrue(photos.isSuccess());
    assertEquals(1, photos.get().size());
    assertTrue(photos.get().contains(testPhoto));
  }

  @Test
  public void shouldReturnFailureWhenAddingNewPhoto() {
    // given
    HashMap<User, List<Photo>> photos = mock(HashMap.class);
    when(photos.get(any())).thenReturn(Option.none());

    PhotoInMemoryDatabase underTest = new PhotoInMemoryDatabase(photos);
    Photo testPhoto = new Photo("test-photo", ANY_FILE, ANY_EVENT_DATE, ANY_EVENT_CREATOR);

    Exception addingPhotoException = new RuntimeException("Error adding a photo");
    when(photos.put(eq(Anys.ANY_USER), eq(List.of(testPhoto)))).thenThrow(addingPhotoException);

    // when
    Try<Photo> result = underTest.addPhoto(testPhoto);

    // then
    assertTrue(result.isFailure());
    assertEquals(addingPhotoException, result.getCause());
  }
}
