package com.bookkeeper.app.application.domain.service;

import com.bookkeeper.app.application.domain.model.Photo;
import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.out.AddPhotoPort;
import com.bookkeeper.app.common.Anys;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {

  @Mock private AddPhotoPort addPhotoPort;

  @InjectMocks private PhotoService underTest;

  @Test
  public void testPhotoSuccessfullyAdded() {

    // given
    UUID id = UUID.randomUUID();
    when(addPhotoPort.addPhoto(any()))
        .thenReturn(
            Try.success(
                new Photo(id, "new-photo", mock(File.class), ZonedDateTime.now(), Anys.ANY_USER)));

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
    when(addPhotoPort.addPhoto(any())).thenReturn(Try.failure(new RuntimeException("Any random failure")));

    // when
    Try<AddPhotoUseCase.Photo> result =
        this.underTest.addPhoto(
            new AddPhotoUseCase.AddPhotoCommand(
                "new-photo", mock(File.class), ZonedDateTime.now(), Anys.ANY_USER));

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }
}
