package com.bookkeeper.app.application.domain.service;

import static com.bookkeeper.app.common.Anys.*;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.bookkeeper.app.application.domain.model.EventDate;
import com.bookkeeper.app.application.domain.model.Photo;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListPhotosUseCase;
import com.bookkeeper.app.application.port.out.AddAttachmentPort;
import com.bookkeeper.app.application.port.out.AddPhotoPort;
import com.bookkeeper.app.application.port.out.ListPhotosPort;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {

  @Mock private AddPhotoPort addPhotoPort;
  @Mock private ListPhotosPort listPhotosPort;
  @Mock private FindUserUseCase findUserUseCase;
  @Mock private AddAttachmentPort addAttachmentPort;

  @InjectMocks private PhotoService underTest;

  @Test
  public void shouldReturnExceptionWhenRetrievingUserFailedWhenAddingPhoto() {

    // given
    RuntimeException userNotFoundException = new RuntimeException("User not found");
    when(findUserUseCase.findUser(ANY_USER_EMAIL)).thenReturn(Try.failure(userNotFoundException));

    // when
    Try<AddPhotoUseCase.Photo> result =
        this.underTest.addPhoto(
            ANY_USER_EMAIL, EventDate.of(ZonedDateTime.now()).get(), ANY_FILE, "new-photo");

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
    VavrAssertions.assertThat(result).failReasonHasMessage(userNotFoundException.getMessage());
  }

  @Test
  public void shouldReturnExceptionWhenUserNotFoundWhenAddingPhoto() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Option.<User>none().toTry());

    // when
    Try<AddPhotoUseCase.Photo> result =
        this.underTest.addPhoto(
            ANY_USER_EMAIL, EventDate.of(ZonedDateTime.now()).get(), ANY_FILE, "new-photo");

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void testAddingPhotoFailedDueToExceptionWhenAddingAttachment() {

    // given
    when(findUserUseCase.findUser(ANY_USER_EMAIL)).thenReturn(Try.success(ANY_USER));
    RuntimeException addingAttachmentFailedException =
        new RuntimeException("Adding attachment failed");
    when(addAttachmentPort.addAttachment(any(), any()))
        .thenReturn(Try.failure(addingAttachmentFailedException));

    // when
    Try<AddPhotoUseCase.Photo> result =
        this.underTest.addPhoto(
            ANY_USER_EMAIL, EventDate.of(ZonedDateTime.now()).get(), ANY_FILE, "new-photo");

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
    VavrAssertions.assertThat(result)
        .failReasonHasMessage(addingAttachmentFailedException.getMessage());
  }

  @Test
  public void testPhotoSuccessfullyAdded() {

    // given
    when(findUserUseCase.findUser(ANY_USER_EMAIL)).thenReturn(Try.success(ANY_USER));
    when(addAttachmentPort.addAttachment(any(), any()))
        .thenReturn(Try.success(ANY_PHOTO_ATTACHMENT));
    Photo photo =
        Photo.create("new-photo", ANY_PHOTO_ATTACHMENT, ANY_EVENT_DATE, ANY_EVENT_CREATOR);
    when(addPhotoPort.addPhoto(any())).thenReturn(Try.success(photo));

    // when
    Try<AddPhotoUseCase.Photo> result =
        this.underTest.addPhoto(
            ANY_USER_EMAIL, EventDate.of(ZonedDateTime.now()).get(), ANY_FILE, "new-photo");

    // then
    assertTrue(result.isSuccess());
    assertEquals(photo.id().value().toString(), result.get().id().toString());
  }

  @Test
  public void testAddingPhotoFailedDueToExceptionWhenAddingPhoto() {

    // given
    when(findUserUseCase.findUser(ANY_USER_EMAIL)).thenReturn(Try.success(ANY_USER));
    when(addAttachmentPort.addAttachment(any(), any()))
        .thenReturn(Try.success(ANY_PHOTO_ATTACHMENT));
    RuntimeException anyRandomFailureException = new RuntimeException("Any random failure");
    when(addPhotoPort.addPhoto(any())).thenReturn(Try.failure(anyRandomFailureException));

    // when
    Try<AddPhotoUseCase.Photo> result =
        this.underTest.addPhoto(
            ANY_USER_EMAIL, EventDate.of(ZonedDateTime.now()).get(), ANY_FILE, "new-photo");

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
    VavrAssertions.assertThat(result).failReasonHasMessage(anyRandomFailureException.getMessage());
  }

  @Test
  public void shouldReturnExceptionWhenRetrievingUserFailedWhenListingPhotos() {

    // given
    when(findUserUseCase.findUser(ANY_USER_EMAIL))
        .thenReturn(Try.failure(new RuntimeException("User not found")));

    // when
    Try<List<ListPhotosUseCase.Photo>> result = this.underTest.listPhotos(ANY_USER_EMAIL);

    // then
    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void shouldReturnExceptionWhenUserNotFoundWhenListingPhotos() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Option.<User>none().toTry());

    // when
    Try<List<ListPhotosUseCase.Photo>> result = this.underTest.listPhotos(ANY_USER_EMAIL);

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void testListPhotosSortedByDate() {

    // given
    when(findUserUseCase.findUser(ANY_USER_EMAIL)).thenReturn(Try.success(ANY_USER));

    EventDate firstDate =
        EventDate.of(LocalDateTime.parse("2009-12-03T10:15:30").atZone(ZoneId.systemDefault()))
            .get();
    EventDate secondDate =
        EventDate.of(LocalDateTime.parse("2024-12-04T10:16:30").atZone(ZoneId.systemDefault()))
            .get();

    when(listPhotosPort.listPhotos(Anys.ANY_USER))
        .thenReturn(
            Try.success(
                List.of(
                    Photo.create("first-photo", ANY_PHOTO_ATTACHMENT, firstDate, ANY_EVENT_CREATOR),
                    Photo.create(
                        "second-photo", ANY_PHOTO_ATTACHMENT, secondDate, ANY_EVENT_CREATOR))));

    // when
    Try<List<ListPhotosUseCase.Photo>> result = this.underTest.listPhotos(ANY_USER_EMAIL);

    // then
    assertTrue(result.isSuccess());
    assertEquals(2, result.get().size());
    assertEquals(firstDate.value(), result.get().get(0).date());
    assertEquals(secondDate.value(), result.get().get(1).date());
  }
}
