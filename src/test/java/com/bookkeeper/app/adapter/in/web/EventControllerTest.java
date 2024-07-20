package com.bookkeeper.app.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookkeeper.app.application.domain.service.EventWithNameExistsException;
import com.bookkeeper.app.application.port.in.AddEventUseCase;
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
@WebMvcTest(EventController.class)
@WithUserDetails(Anys.ANY_EMAIL)
class EventControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private AddEventUseCase addEventUseCase;

  @MockBean private FindUserUseCase findUserUseCase;

  @Test
  public void shouldReturnRequestErrorWhenOwnerIsNotFound() throws Exception {

    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new Exception("Cannot find the owner")));

    // when & then
    this.mockMvc
        .perform(
            post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new EventController.Event("any-name"))))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot find the owner")));
  }

  @Test
  public void shouldReturnRequestErrorWhenEventAdditionFailedDueToAnyError() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(addEventUseCase.addEvent(new AddEventUseCase.AddEventCommand("any-name", Anys.ANY_USER)))
        .thenReturn(Try.failure(new Exception("Cannot add a event")));

    // when & then
    this.mockMvc
        .perform(
            post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new EventController.Event("any-name"))))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot add a event")));
  }

  @Test
  public void shouldReturnRequestConflictWhenEventAdditionFailedDueEventWithNameExistsException()
      throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(addEventUseCase.addEvent(new AddEventUseCase.AddEventCommand("any-name", Anys.ANY_USER)))
        .thenReturn(Try.failure(new EventWithNameExistsException("Cannot add a event")));

    // when & then
    this.mockMvc
        .perform(
            post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new EventController.Event("any-name"))))
        .andDo(print())
        .andExpect(status().isConflict())
        .andExpect(content().string(containsString("Cannot add a event")));
  }

  @Test
  public void shouldReturnCreatedWhenEventAdditionSucceeded() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    UUID id = UUID.randomUUID();
    AddEventUseCase.Event event = new AddEventUseCase.Event(id, "event-name");
    when(addEventUseCase.addEvent(new AddEventUseCase.AddEventCommand(event.name(), Anys.ANY_USER)))
        .thenReturn(Try.success(event));

    // when & then
    this.mockMvc
        .perform(
            post("/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new EventController.Event(event.name()))))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(id.toString())))
        .andExpect(content().string(containsString(event.name())));
  }

  private final String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
