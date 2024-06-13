package com.bookkeeper.app.application.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bookkeeper.app.application.domain.model.Shelf;
import com.bookkeeper.app.application.port.in.AddShelfUseCase;
import com.bookkeeper.app.application.port.out.AddShelfPort;
import com.bookkeeper.app.application.port.out.ListShelvesPort;
import com.bookkeeper.app.common.Anys;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShelfServiceTest {
  @Mock private AddShelfPort addShelfPort;

  @Mock private ListShelvesPort listShelvesPort;

  @InjectMocks private ShelfService underTest;

  @Test
  public void testShelfSuccessfullyAdded() {

    when(listShelvesPort.listShelves(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addShelfPort.addShelf(any()))
        .thenReturn(Try.success(new Shelf("new-shelf", Anys.ANY_USER)));

    Try<AddShelfUseCase.Shelf> result =
        this.underTest.addShelf(new AddShelfUseCase.AddShelfCommand("new-shelf", Anys.ANY_USER));

    assertTrue(result.isSuccess());
    assertEquals("new-shelf", result.get().name());
  }

  @Test
  public void testAddingShelfFailedDueToNameOverlap() {

    when(listShelvesPort.listShelves(Anys.ANY_USER))
        .thenReturn(
            Try.success(
                List.of(
                    new Shelf("old-shelf", Anys.ANY_USER), new Shelf("new-shelf", Anys.ANY_USER))));

    Try<AddShelfUseCase.Shelf> result =
        this.underTest.addShelf(new AddShelfUseCase.AddShelfCommand("new-shelf", Anys.ANY_USER));

    assertTrue(result.isFailure());
    assertInstanceOf(ShelfWithNameExistsException.class, result.getCause());
  }

  @Test
  public void testAddingShelfFailedDueToExceptionWhenAddingTheShelf() {

    when(listShelvesPort.listShelves(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addShelfPort.addShelf(any())).thenThrow(new RuntimeException("Any random failure"));

    Try<AddShelfUseCase.Shelf> result =
        this.underTest.addShelf(new AddShelfUseCase.AddShelfCommand("new-shelf", Anys.ANY_USER));

    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void testAddingDefaultShelvesWithOneError() {

    when(listShelvesPort.listShelves(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addShelfPort.addShelf(any()))
        .thenReturn(Try.success(new Shelf("New", Anys.ANY_USER)))
        .thenThrow(new RuntimeException("Any random failure"));

    Try<List<AddShelfUseCase.Shelf>> result =
        this.underTest.addDefaultShelves(
            new AddShelfUseCase.AddDefaultShelvesCommand(Anys.ANY_USER));

    assertTrue(result.isFailure());
    assertInstanceOf(RuntimeException.class, result.getCause());
  }

  @Test
  public void testAddingDefaultShelvesWithoutAnyError() {

    when(listShelvesPort.listShelves(Anys.ANY_USER)).thenReturn(Try.success(List.empty()));
    when(addShelfPort.addShelf(any()))
        .thenReturn(Try.success(new Shelf("New", Anys.ANY_USER)))
        .thenReturn(Try.success(new Shelf("Scanned", Anys.ANY_USER)));

    Try<List<AddShelfUseCase.Shelf>> result =
        this.underTest.addDefaultShelves(
            new AddShelfUseCase.AddDefaultShelvesCommand(Anys.ANY_USER));

    assertTrue(result.isSuccess());
    assertEquals(2, result.get().size());
    verify(addShelfPort, times(2)).addShelf(any());
  }
}
