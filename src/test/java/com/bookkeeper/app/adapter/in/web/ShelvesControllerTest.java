package com.bookkeeper.app.adapter.in.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookkeeper.app.adapter.in.web.security.JwtAuthenticationFilter;
import com.bookkeeper.app.application.port.in.ListShelvesUseCase;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@WebMvcTest(
    value = ShelvesController.class,
    excludeAutoConfiguration = {
      SecurityAutoConfiguration.class,
      SecurityFilterAutoConfiguration.class
    },
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            value = JwtAuthenticationFilter.class))
class ShelvesControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ListShelvesUseCase listShelvesUseCase;

  @Test
  public void shouldReturnEmptyListWhenNoShelvesArePresent() throws Exception {

    // given
    when(listShelvesUseCase.listShelves(new ListShelvesUseCase.ListShelvesCommand()))
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
    ListShelvesUseCase.Shelf shelf1 = new ListShelvesUseCase.Shelf(UUID.randomUUID(), "shelf-1");
    ListShelvesUseCase.Shelf shelf2 = new ListShelvesUseCase.Shelf(UUID.randomUUID(), "shelf-2");
    when(listShelvesUseCase.listShelves(new ListShelvesUseCase.ListShelvesCommand()))
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
    when(listShelvesUseCase.listShelves(new ListShelvesUseCase.ListShelvesCommand()))
        .thenReturn(Try.failure(new RuntimeException("Cannot retrieve shelves")));

    // when & then
    this.mockMvc
        .perform(get("/shelves"))
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
