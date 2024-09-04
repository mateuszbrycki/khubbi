package com.khubbi.app.application.domain.service;

import static com.khubbi.app.common.Anys.*;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.khubbi.app.application.domain.model.EventAttachment;
import com.khubbi.app.application.domain.model.EventAttachmentId;
import com.khubbi.app.application.domain.model.User;
import com.khubbi.app.application.port.in.FindAttachmentUseCase;
import com.khubbi.app.application.port.in.FindUserUseCase;
import com.khubbi.app.application.port.out.FindAttachmentPort;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttachmentServiceTest {

  @Mock private FindUserUseCase findUserUseCase;
  @Mock private FindAttachmentPort findAttachmentPort;

  @InjectMocks private AttachmentService underTest;

  @Test
  public void shouldReturnExceptionWhenRetrievingUserFailed() {
    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new RuntimeException("User not found")));

    // when
    Try<FindAttachmentUseCase.Attachment> result =
        underTest.findAttachment(ANY_USER_EMAIL, EventAttachmentId.random());

    // then
    assertThat(result).isFailure();
    assertThat(result).failReasonHasMessage("User not found");
  }

  @Test
  public void shouldReturnExceptionWhenUserNotFound() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Option.<User>none().toTry());

    // when
    Try<FindAttachmentUseCase.Attachment> result =
        underTest.findAttachment(ANY_USER_EMAIL, EventAttachmentId.random());

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void shouldReturnExceptionWhenRetrievingPhotoFailed() {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(ANY_USER));
    EventAttachmentId eventAttachmentId = EventAttachmentId.random();
    when(findAttachmentPort.findById(eq(ANY_USER), eq(eventAttachmentId)))
        .thenReturn(Try.failure(new RuntimeException("An error occurred")));

    // when
    Try<FindAttachmentUseCase.Attachment> result =
        underTest.findAttachment(ANY_USER_EMAIL, eventAttachmentId);

    // then
    assertThat(result).isFailure();
    assertThat(result).failReasonHasMessage("An error occurred");
  }

  @Test
  public void shouldReturnAttachmentWhenFound() {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(ANY_USER));

    EventAttachmentId eventAttachmentId = EventAttachmentId.random();
    when(findAttachmentPort.findById(eq(ANY_USER), eq(eventAttachmentId)))
        .thenReturn(Try.success(new EventAttachment.PhotoAttachment(eventAttachmentId, ANY_FILE)));

    // when
    Try<FindAttachmentUseCase.Attachment> result =
        underTest.findAttachment(ANY_USER_EMAIL, eventAttachmentId);

    // then
    assertThat(result).isSuccess();
    assertThat(result).contains(new FindAttachmentUseCase.Attachment(eventAttachmentId, ANY_FILE));
  }
}
