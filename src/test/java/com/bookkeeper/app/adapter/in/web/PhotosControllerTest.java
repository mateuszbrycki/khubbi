package com.bookkeeper.app.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.domain.model.UserEmail;
import com.bookkeeper.app.application.port.in.ListPhotosUseCase;
import com.bookkeeper.app.common.Anys;
import com.bookkeeper.app.common.TestSecurityConfiguration;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfiguration.class)
@WebMvcTest(PhotosController.class)
@WithUserDetails(Anys.ANY_EMAIL)
class PhotosControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ListPhotosUseCase listPhotosUseCase;

  @Test
  public void shouldReturnEmptyListWhenNoPhotosArePresent() throws Exception {

    // given
    when(listPhotosUseCase.listPhotos(any(UserEmail.class))).thenReturn(Try.success(List.empty()));

    // when & then
    this.mockMvc
        .perform(get("/photos"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  public void shouldReturnListWithTwoElementsWhenTwoPhotosArePresent() throws Exception {

    // given

    ListPhotosUseCase.Photo photo1 =
        new ListPhotosUseCase.Photo(
            UUID.randomUUID(),
            "photo-1",
            EventAttachmentId.random().value().toString(),
            ZonedDateTime.now());
    ListPhotosUseCase.Photo photo2 =
        new ListPhotosUseCase.Photo(
            UUID.randomUUID(),
            "photo-2",
            EventAttachmentId.random().value().toString(),
            ZonedDateTime.now());
    when(listPhotosUseCase.listPhotos(any(UserEmail.class)))
        .thenReturn(Try.success(List.of(photo1, photo2)));

    // when & then
    this.mockMvc
        .perform(get("/photos"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$[0].id").value(photo1.id().toString()))
        .andExpect(jsonPath("$[0].description").value(photo1.description()))
        .andExpect(jsonPath("$[0].url").value("http://localhost:8080/attachment/" + photo1.url()))
        .andExpect(
            jsonPath("$[0].date")
                .value(photo1.date().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
        .andExpect(jsonPath("$[1].id").value(photo2.id().toString()))
        .andExpect(jsonPath("$[1].description").value(photo2.description()))
        .andExpect(jsonPath("$[1].url").value("http://localhost:8080/attachment/" + photo2.url()))
        .andExpect(
            jsonPath("$[1].date")
                .value(photo2.date().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
  }

  @Test
  public void shouldReturnInternalServerErrorWhenRetrivalFailed() throws Exception {

    // given
    when(listPhotosUseCase.listPhotos(any(UserEmail.class)))
        .thenReturn(Try.failure(new RuntimeException("Cannot retrieve photos")));

    // when & then
    this.mockMvc.perform(get("/photos")).andDo(print()).andExpect(status().isInternalServerError());
  }
}
