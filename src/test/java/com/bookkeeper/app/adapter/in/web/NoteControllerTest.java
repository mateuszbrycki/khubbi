package com.bookkeeper.app.adapter.in.web;

import static com.bookkeeper.app.common.JsonUtils.asJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookkeeper.app.application.port.in.AddNoteUseCase;
import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.common.Anys;
import com.bookkeeper.app.common.TestSecurityConfiguration;
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

  @MockBean private FindUserUseCase findUserUseCase;

  @Test
  public void shouldReturnRequestErrorWhenOwnerIsNotFound() throws Exception {

    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new Exception("Cannot find the owner")));

    // when & then
    this.mockMvc
        .perform(
            post("/note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    asJsonString(
                        new NoteController.AddNoteRequest(
                            new NoteController.Payload("any-name"), ZonedDateTime.now()))))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot find the owner")));
  }

  @Test
  public void shouldReturnRequestErrorWhenNoteAdditionFailedDueToAnyError() throws Exception {

    // given
    ZonedDateTime date = ZonedDateTime.now();
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(addNoteUseCase.addNote(
            AddNoteUseCase.AddNoteCommand.builder()
                .note("any-name")
                .date(date)
                .owner(Anys.ANY_USER)
                .build()))
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
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    ZonedDateTime date = ZonedDateTime.now();
    UUID id = UUID.randomUUID();
    AddNoteUseCase.Note note = new AddNoteUseCase.Note(id, "note-name", date);
    when(addNoteUseCase.addNote(
            AddNoteUseCase.AddNoteCommand.builder()
                .note(note.note())
                .date(date)
                .owner(Anys.ANY_USER)
                .build()))
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
