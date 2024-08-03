package com.bookkeeper.app.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListEventsUseCase;
import com.bookkeeper.app.common.Anys;
import com.bookkeeper.app.common.TestSecurityConfiguration;
import io.vavr.collection.HashMap;
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
@WebMvcTest(EventsController.class)
@WithUserDetails(Anys.ANY_EMAIL)
class EventsControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ListEventsUseCase listEventsUseCase;

  @MockBean private FindUserUseCase findUserUseCase;

  @Test
  public void shouldReturnRequestErrorWhenOwnerIsNotFound() throws Exception {

    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new Exception("Cannot find the owner")));

    // when & then
    this.mockMvc
        .perform(get("/events"))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot find the owner")));
  }

  @Test
  public void shouldReturnEmptyListWhenNoNotesArePresent() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(listEventsUseCase.listEvents(new ListEventsUseCase.ListEventsQuery(any())))
        .thenReturn(Try.success(List.empty()));

    // when & then
    this.mockMvc
        .perform(get("/events"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  public void shouldReturnListWithTwoElementsWhenTwoEventsArePresent() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));

    ListEventsUseCase.Event event1 =
        new ListEventsUseCase.Event(
            UUID.randomUUID(), ZonedDateTime.now(), HashMap.of("note", "note-1"));
    ListEventsUseCase.Event event2 =
        new ListEventsUseCase.Event(
            UUID.randomUUID(),
            ZonedDateTime.now(),
            HashMap.of("description", "description-1", "photo", "photo-1"));
    when(listEventsUseCase.listEvents(new ListEventsUseCase.ListEventsQuery(any())))
        .thenReturn(Try.success(List.of(event1, event2)));

    // when & then
    // TODO mateusz.brycki asssert properties
    this.mockMvc
        .perform(get("/events"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$[0].id").value(event1.id().toString()))
        .andExpect(
            jsonPath("$[0].properties.note")
                .value(
                    event1
                        .properties()
                        .get("note")
                        .getOrElse("no note property in the event object")))
        .andExpect(
            jsonPath("$[0].date")
                .value(event1.date().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
        .andExpect(jsonPath("$[1].id").value(event2.id().toString()))
        .andExpect(
            jsonPath("$[1].properties.description")
                .value(
                    event2
                        .properties()
                        .get("description")
                        .getOrElse("no description property in the event object")))
        .andExpect(
            jsonPath("$[1].properties.photo")
                .value(
                    event2
                        .properties()
                        .get("photo")
                        .getOrElse("no photo property in the event object")))
        .andExpect(
            jsonPath("$[1].date")
                .value(event2.date().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
  }

  @Test
  public void shouldReturnInternalServerErrorWhenRetrievalFailed() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(listEventsUseCase.listEvents(new ListEventsUseCase.ListEventsQuery(any())))
        .thenReturn(Try.failure(new RuntimeException("Cannot retrieve notes")));

    // when & then
    this.mockMvc.perform(get("/events")).andDo(print()).andExpect(status().isInternalServerError());
  }
}
