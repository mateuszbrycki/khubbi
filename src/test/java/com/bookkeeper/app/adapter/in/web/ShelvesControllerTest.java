package com.bookkeeper.app.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookkeeper.app.application.port.in.FindUserUseCase;
import com.bookkeeper.app.application.port.in.ListShelvesUseCase;
import com.bookkeeper.app.common.Anys;
import com.bookkeeper.app.common.TestSecurityConfiguration;
import io.vavr.collection.List;
import io.vavr.control.Try;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@Import(TestSecurityConfiguration.class)
@WebMvcTest(ShelvesController.class)
@WithUserDetails(Anys.ANY_EMAIL)
class ShelvesControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ListShelvesUseCase listShelvesUseCase;

  @MockBean private FindUserUseCase findUserUseCase;

  @Test
  public void shouldReturnRequestErrorWhenOwnerIsNotFound() throws Exception {

    // given
    when(findUserUseCase.findUser(any()))
        .thenReturn(Try.failure(new Exception("Cannot find the owner")));

    // when & then
    this.mockMvc
        .perform(get("/shelves"))
        .andDo(print())
        .andExpect(status().isInternalServerError())
        .andExpect(content().string(containsString("Cannot find the owner")));
  }

  @Test
  public void shouldReturnEmptyListWhenNoShelvesArePresent() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(listShelvesUseCase.listShelves(new ListShelvesUseCase.ListShelvesCommand(any())))
        .thenReturn(Try.success(List.empty()));

    // when & then
    this.mockMvc
        .perform(get("/shelves"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$").isEmpty());
  }

  @Test
  public void shouldReturnListWithTwoElementsWhenTwoShelvesArePresent() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));

    ListShelvesUseCase.Shelf shelf1 = new ListShelvesUseCase.Shelf(UUID.randomUUID(), "shelf-1");
    ListShelvesUseCase.Shelf shelf2 = new ListShelvesUseCase.Shelf(UUID.randomUUID(), "shelf-2");
    when(listShelvesUseCase.listShelves(new ListShelvesUseCase.ListShelvesCommand(any())))
        .thenReturn(Try.success(List.of(shelf1, shelf2)));

    // when & then
    this.mockMvc
        .perform(get("/shelves"))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$[0].id").value(shelf1.id().toString()))
        .andExpect(jsonPath("$[0].name").value(shelf1.name()))
        .andExpect(jsonPath("$[1].id").value(shelf2.id().toString()))
        .andExpect(jsonPath("$[1].name").value(shelf2.name()));
  }

  @Test
  public void shouldReturnInternalServerErrorWhenRetrivalFailed() throws Exception {

    // given
    when(findUserUseCase.findUser(any())).thenReturn(Try.success(Anys.ANY_USER));
    when(listShelvesUseCase.listShelves(new ListShelvesUseCase.ListShelvesCommand(any())))
        .thenReturn(Try.failure(new RuntimeException("Cannot retrieve shelves")));

    // when & then
    this.mockMvc
        .perform(get("/shelves"))
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
