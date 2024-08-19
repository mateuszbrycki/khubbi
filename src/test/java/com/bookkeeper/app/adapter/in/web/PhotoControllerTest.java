package com.bookkeeper.app.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.common.Anys;
import com.bookkeeper.app.common.TestSecurityConfiguration;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@Import({TestSecurityConfiguration.class})
@WebMvcTest(PhotoController.class)
@WithUserDetails(Anys.ANY_EMAIL)
public class PhotoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AddPhotoUseCase addPhotoUseCase;

  private MockMultipartFile FILE = new MockMultipartFile("test-file.png", "test-file".getBytes());

  @Test
  public void shouldReturnRequestErrorWhenPhotoAdditionFailedDueToAnyError() throws Exception {
    // given
    when(addPhotoUseCase.addPhoto(any(), any(), any(), any()))
        .thenReturn(Try.failure(new Exception("An error occurred")));

    // when & then
    this.mockMvc
        .perform(
            multipart("/photo")
                .file("payload.photo", FILE.getBytes())
                .param("payload.description", "any-description")
                .param("date", ZonedDateTime.now().toString()))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("An error occurred")));
  }

  @Test
  public void shouldReturnCreatedWhenPhotoAdditionSucceeded() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    ZonedDateTime date = ZonedDateTime.now();
    when(addPhotoUseCase.addPhoto(any(), any(), any(), any()))
        .thenReturn(
            Try.success(new AddPhotoUseCase.Photo(id, EventAttachmentId.random().value(), date)));

    // when & then
    this.mockMvc
        .perform(
            multipart("/photo")
                .file("payload.photo", FILE.getBytes())
                .param("payload.description", "any-description")
                .param("date", ZonedDateTime.now().toString()))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(id.toString())))
        .andExpect(
            content().string(containsString(date.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))));
  }
}
