package com.bookkeeper.app.adapter.in.web;

import com.bookkeeper.app.application.port.in.AddPhotoUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.common.Anys;
import com.bookkeeper.app.common.TestSecurityConfiguration;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.bookkeeper.app.common.JsonUtils.asJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestSecurityConfiguration.class})
@WebMvcTest(PhotoController.class)
@WithUserDetails(Anys.ANY_EMAIL)
public class PhotoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private FindUserUseCase findUserUseCase;
  @MockBean private AddPhotoUseCase addPhotoUseCase;

  @Test
  public void shouldReturnRequestErrorWhenOwnerIsNotFound() throws Exception {
    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new Exception("Cannot find the owner")));

    // when & then
    this.mockMvc
        .perform(
            post("/photo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    asJsonString(
                        new PhotoController.Photo(
                            "any-description", mock(File.class), ZonedDateTime.now()))))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot find the owner")));
  }

  @Test
  public void shouldReturnRequestErrorWhenPhotoAdditionFailedDueToAnyError() throws Exception {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(addPhotoUseCase.addPhoto(any()))
        .thenReturn(Try.failure(new Exception("An error occurred")));

    // when & then
    this.mockMvc
        .perform(
            post("/photo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    asJsonString(
                        new PhotoController.Photo(
                            "any-description", mock(File.class), ZonedDateTime.now()))))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("An error occurred")));
  }

  @Test
  public void shouldReturnCreatedWhenPhotoAdditionSucceeded() throws Exception {
    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    UUID id = UUID.randomUUID();
    ZonedDateTime date = ZonedDateTime.now();
    when(addPhotoUseCase.addPhoto(any()))
        .thenReturn(Try.success(new AddPhotoUseCase.Photo(id, "https://any-url", date)));

    // when & then
    this.mockMvc
        .perform(
            post("/photo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    asJsonString(
                        new PhotoController.Photo(
                            "any-description", mock(File.class), ZonedDateTime.now()))))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(id.toString())))
        .andExpect(
            content().string(containsString(date.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))));
  }
}
