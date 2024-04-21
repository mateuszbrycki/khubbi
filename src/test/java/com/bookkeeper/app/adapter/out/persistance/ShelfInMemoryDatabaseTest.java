package com.bookkeeper.app.adapter.out.persistance;

import com.bookkeeper.app.application.domain.model.Shelf;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShelfInMemoryDatabaseTest {

  @Test
  public void shouldReturnEmptyListWhenNoShelvesAdded() {
    // given
    ShelfInMemoryDatabase underTest = new ShelfInMemoryDatabase(List.empty());

    // when
    Try<List<Shelf>> result = underTest.listShelves();

    // then
    assertTrue(result.isSuccess());
    assertTrue(result.get().isEmpty());
  }

  @Test
  public void shouldAddNewShelf() {

    // given
    ShelfInMemoryDatabase underTest = new ShelfInMemoryDatabase(List.empty());
    Shelf testShelf = new Shelf("test-shelf");

    // when
    Try<Shelf> result = underTest.addShelf(testShelf);
    Try<List<Shelf>> shelves = underTest.listShelves();

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
    List<Shelf> shelves = mock(List.class);

    ShelfInMemoryDatabase underTest = new ShelfInMemoryDatabase(shelves);
    Shelf testShelf = new Shelf("test-shelf");

    Exception addingShelfException = new RuntimeException("Error adding a shelf");
    when(shelves.append(testShelf)).thenThrow(addingShelfException);

    // when
    Try<Shelf> result = underTest.addShelf(testShelf);

    // then
    assertTrue(result.isFailure());
    assertEquals(addingShelfException, result.getCause());
  }
}
