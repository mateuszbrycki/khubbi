package com.khubbi.app.adapter.in.web;

import static com.khubbi.app.common.Anys.ANY_USER_EMAIL;
import static com.khubbi.app.common.JsonUtils.asJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.khubbi.app.application.domain.model.EventDate;
import com.khubbi.app.application.port.in.AddNoteUseCase;
import com.khubbi.app.common.Anys;
import com.khubbi.app.common.TestSecurityConfiguration;
import io.vavr.control.Try;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@Import({TestSecurityConfiguration.class})
@WebMvcTest(NoteController.class)
@WithUserDetails(Anys.ANY_EMAIL)
class NoteControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private AddNoteUseCase addNoteUseCase;

  @Test
  public void shouldReturnRequestErrorWhenNoteAdditionFailedDueToAnyError() throws Exception {

    // given
    ZonedDateTime date = ZonedDateTime.now();
    when(addNoteUseCase.addNote(ANY_USER_EMAIL, EventDate.of(date).get(), "any-name"))
        .thenReturn(Try.failure(new Exception("Cannot add a note")));

    // when & then
    this.mockMvc
        .perform(
            post("/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    asJsonString(
                        new NoteController.AddNoteRequest(
                            new NoteController.Payload("any-name"), date))))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot add a note")));
  }

  @Test
  public void shouldReturnCreatedWhenNoteAdditionSucceeded() throws Exception {

    // given
    ZonedDateTime date = ZonedDateTime.now();
    UUID id = UUID.randomUUID();
    AddNoteUseCase.Note note = new AddNoteUseCase.Note(id, "note-name", date);
    when(addNoteUseCase.addNote(ANY_USER_EMAIL, EventDate.of(date).get(), note.note()))
        .thenReturn(Try.success(note));

    // when & then
    this.mockMvc
        .perform(
            post("/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    asJsonString(
                        new NoteController.AddNoteRequest(
                            new NoteController.Payload(note.note()), date))))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString(id.toString())))
        .andExpect(content().string(containsString(note.note())));
  }
}
