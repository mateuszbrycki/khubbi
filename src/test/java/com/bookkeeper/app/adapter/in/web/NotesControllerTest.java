package com.bookkeeper.app.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookkeeper.app.application.port.in.ListNotesUseCase;
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
@WebMvcTest(NotesController.class)
@WithUserDetails(Anys.ANY_EMAIL)
class NotesControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ListNotesUseCase listNotesUseCase;

  @Test
  public void shouldReturnEmptyListWhenNoNotesArePresent() throws Exception {

    // given
    when(listNotesUseCase.listNotes(any(ListNotesUseCase.ListNotesQuery.class)))
        .thenReturn(Try.success(List.empty()));

    // when & then
    this.mockMvc
        .perform(get("/notes"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  public void shouldReturnListWithTwoElementsWhenTwoNotesArePresent() throws Exception {

    // given

    ListNotesUseCase.Note note1 =
        new ListNotesUseCase.Note(UUID.randomUUID(), "note-1", ZonedDateTime.now());
    ListNotesUseCase.Note note2 =
        new ListNotesUseCase.Note(UUID.randomUUID(), "note-2", ZonedDateTime.now());
    when(listNotesUseCase.listNotes(any(ListNotesUseCase.ListNotesQuery.class)))
        .thenReturn(Try.success(List.of(note1, note2)));

    // when & then
    this.mockMvc
        .perform(get("/notes"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$[0].id").value(note1.id().toString()))
        .andExpect(jsonPath("$[0].note").value(note1.note()))
        .andExpect(
            jsonPath("$[0].date")
                .value(note1.date().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
        .andExpect(jsonPath("$[1].id").value(note2.id().toString()))
        .andExpect(jsonPath("$[1].note").value(note2.note()))
        .andExpect(
            jsonPath("$[1].date")
                .value(note2.date().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
  }

  @Test
  public void shouldReturnInternalServerErrorWhenRetrievalFailed() throws Exception {

    // given
    when(listNotesUseCase.listNotes(any(ListNotesUseCase.ListNotesQuery.class)))
        .thenReturn(Try.failure(new RuntimeException("Cannot retrieve notes")));

    // when & then
    this.mockMvc.perform(get("/notes")).andDo(print()).andExpect(status().isInternalServerError());
  }
}
