package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.Shelf;
import com.bookkeeper.app.application.domain.model.User;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShelfInMemoryDatabaseTest {

  @Test
  public void shouldReturnEmptyListWhenNoShelvesAdded() {
    // given
    ShelfInMemoryDatabase underTest = new ShelfInMemoryDatabase(HashMap.empty());

    // when
    Try<List<Shelf>> result = underTest.listShelves(Anys.ANY_USER);

    // then
    assertTrue(result.isSuccess());
    assertTrue(result.get().isEmpty());
  }

  @Test
  public void shouldAddNewShelf() {

    // given
    ShelfInMemoryDatabase underTest = new ShelfInMemoryDatabase(HashMap.empty());
    Shelf testShelf = new Shelf("test-shelf", Anys.ANY_USER);

    // when
    Try<Shelf> result = underTest.addShelf(testShelf);
    Try<List<Shelf>> shelves = underTest.listShelves(Anys.ANY_USER);

    // then
    assertTrue(result.isSuccess());
    assertEquals(testShelf, result.get());

    assertTrue(shelves.isSuccess());
    assertEquals(1, shelves.get().size());
    assertTrue(shelves.get().contains(testShelf));
  }

  @Test
  public void shouldReturnFailureWhenAddingNewShelf() {
    // given
    HashMap<User, List<Shelf>> shelves = mock(HashMap.class);
    when(shelves.get(any())).thenReturn(Option.none());

    ShelfInMemoryDatabase underTest = new ShelfInMemoryDatabase(shelves);
    Shelf testShelf = new Shelf("test-shelf", Anys.ANY_USER);

    Exception addingShelfException = new RuntimeException("Error adding a shelf");
    when(shelves.put(eq(Anys.ANY_USER), eq(List.of(testShelf)))).thenThrow(addingShelfException);

    // when
    Try<Shelf> result = underTest.addShelf(testShelf);

    // then
    assertTrue(result.isFailure());
    assertEquals(addingShelfException, result.getCause());
  }
}
