package com.bookkeeper.app.application.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bookkeeper.app.application.domain.model.EventCreator;
import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.EventId;
import com.bookkeeper.app.application.domain.model.Photo;
import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.in.ListPhotosUseCase;
import com.bookkeeper.app.application.port.out.AddPhotoPort;
import com.bookkeeper.app.application.port.out.ListPhotosPort;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {

  @Mock private AddPhotoPort addPhotoPort;
  @Mock private ListPhotosPort listPhotosPort;

  @InjectMocks private PhotoService underTest;

  @Test
  public void testPhotoSuccessfullyAdded() {

    // given
    UUID id = UUID.randomUUID();
    when(addPhotoPort.addPhoto(any()))
        .thenReturn(
            Try.success(
                new Photo(
                    EventId.of(id),
                    "new-photo",
                    mock(File.class),
                    EventDate.of(ZonedDateTime.now()),
                    EventCreator.of(Anys.ANY_USER))));

    // when
    Try<AddPhotoUseCase.Photo> result =
        this.underTest.addPhoto(
            new AddPhotoUseCase.AddPhotoCommand(
                "new-photo", mock(File.class), ZonedDateTime.now(), Anys.ANY_USER));

    // then
    assertTrue(result.isSuccess());
    assertEquals(id.toString(), result.get().id().toString());
  }

  @Test
  public void testAddingPhotoFailedDueToExceptionWhenAddingThePhoto() {

    // given
    when(addPhotoPort.addPhoto(any()))
        .thenReturn(Try.failure(new RuntimeException("Any random failure")));

    // when
    Try<AddPhotoUseCase.Photo> result =
        this.underTest.addPhoto(
            new AddPhotoUseCase.AddPhotoCommand(
                "new-photo", mock(File.class), ZonedDateTime.now(), Anys.ANY_USER));

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void testListPhotosSortedByDate() {

    // given
    EventDate firstDate =
        EventDate.of(LocalDateTime.parse("2009-12-03T10:15:30").atZone(ZoneId.systemDefault()));
    EventDate secondDate =
        EventDate.of(LocalDateTime.parse("2024-12-04T10:16:30").atZone(ZoneId.systemDefault()));

    when(listPhotosPort.listPhotos(Anys.ANY_USER))
        .thenReturn(
            Try.success(
                List.of(
                    new Photo(
                        "first-photo", mock(File.class), firstDate, EventCreator.of(Anys.ANY_USER)),
                    new Photo(
                        "second-photo",
                        mock(File.class),
                        secondDate,
                        EventCreator.of(Anys.ANY_USER)))));

    // when
    Try<List<ListPhotosUseCase.Photo>> result =
        this.underTest.listPhotos(new ListPhotosUseCase.ListPhotosQuery(Anys.ANY_USER));

    // then
    assertTrue(result.isSuccess());
    assertEquals(2, result.get().size());
    assertEquals(firstDate.value(), result.get().get(0).date());
    assertEquals(secondDate.value(), result.get().get(1).date());
  }
}
