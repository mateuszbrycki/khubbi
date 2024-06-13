package com.bookkeeper.app.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookkeeper.app.application.domain.service.ShelfWithNameExistsException;
import com.bookkeeper.app.application.port.in.AddShelfUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.common.Anys;
import com.bookkeeper.app.common.TestSecurityConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfiguration.class)
@WebMvcTest(ShelfController.class)
@WithUserDetails(Anys.ANY_EMAIL)
class ShelfControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private AddShelfUseCase addShelfUseCase;

  @MockBean private FindUserUseCase findUserUseCase;

  @Test
  public void shouldReturnRequestErrorWhenOwnerIsNotFound() throws Exception {

    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new Exception("Cannot find the owner")));

    // when & then
    this.mockMvc
        .perform(
            post("/shelf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new ShelfController.Shelf("any-name"))))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot find the owner")));
  }

  @Test
  public void shouldReturnRequestErrorWhenShelfAdditionFailedDueToAnyError() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(addShelfUseCase.addShelf(new AddShelfUseCase.AddShelfCommand("any-name", Anys.ANY_USER)))
        .thenReturn(Try.failure(new Exception("Cannot add a shelf")));

    // when & then
    this.mockMvc
        .perform(
            post("/shelf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new ShelfController.Shelf("any-name"))))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot add a shelf")));
  }

  @Test
  public void shouldReturnRequestConflictWhenShelfAdditionFailedDueShelfWithNameExistsException()
      throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(addShelfUseCase.addShelf(new AddShelfUseCase.AddShelfCommand("any-name", Anys.ANY_USER)))
        .thenReturn(Try.failure(new ShelfWithNameExistsException("Cannot add a shelf")));

    // when & then
    this.mockMvc
        .perform(
            post("/shelf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new ShelfController.Shelf("any-name"))))
        .andDo(print())
        .andExpect(status().isConflict())
        .andExpect(content().string(containsString("Cannot add a shelf")));
  }

  @Test
  public void shouldReturnCreatedWhenShelfAdditionSucceeded() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    UUID id = UUID.randomUUID();
    AddShelfUseCase.Shelf shelf = new AddShelfUseCase.Shelf(id, "shelf-name");
    when(addShelfUseCase.addShelf(new AddShelfUseCase.AddShelfCommand(shelf.name(), Anys.ANY_USER)))
        .thenReturn(Try.success(shelf));

    // when & then
    this.mockMvc
        .perform(
            post("/shelf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new ShelfController.Shelf(shelf.name()))))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(id.toString())))
        .andExpect(content().string(containsString(shelf.name())));
  }

  private final String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
