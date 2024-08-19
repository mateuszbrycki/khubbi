package com.bookkeeper.app.adapter.out.persistance;

import static com.bookkeeper.app.common.Anys.*;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookkeeper.app.application.domain.model.EventAttachment;
import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

public class AttachmentsInMemoryDatabaseTest {

  @Test
  public void shouldReturnFailureWhenNoAttachmentsAdded() {
    // given
    AttachmentsInMemoryDatabase underTest = new AttachmentsInMemoryDatabase(HashMap.empty());

    // when
    Try<EventAttachment<?>> result = underTest.findById(ANY_USER, EventAttachmentId.random());

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void shouldReturnFailureWhenAttachmentDoesntExist() {
    // given
    AttachmentsInMemoryDatabase underTest =
        new AttachmentsInMemoryDatabase(
            HashMap.of(ANY_USER, List.of(new TestAttachment("Any content"))));

    // when
    Try<EventAttachment<?>> result = underTest.findById(ANY_USER, EventAttachmentId.random());

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void shouldReturnFailureWhenAttachmentBelongsToAnotherUser() {
    // given
    AttachmentsInMemoryDatabase underTest =
        new AttachmentsInMemoryDatabase(
            HashMap.of(ANY_USER, List.of(new TestAttachment("Any content"))));

    // when
    Try<EventAttachment<?>> result =
        underTest.findById(ANY_ANOTHER_USER, EventAttachmentId.random());

    // then
    assertThat(result).isFailure();
  }

  @Test
  public void shouldReturnAttachmentWhenAttachmentExists() {
    // given
    TestAttachment testAttachment = new TestAttachment("Any content");
    AttachmentsInMemoryDatabase underTest =
        new AttachmentsInMemoryDatabase(HashMap.of(ANY_USER, List.of(testAttachment)));

    // when
    Try<EventAttachment<?>> result = underTest.findById(ANY_USER, testAttachment.id());

    // then
    assertThat(result).isSuccess();
    assertEquals(result.get(), testAttachment);
  }

  @Test
  public void shouldReturnAttachmentWhenMultipleAttachmentExists() {
    // given
    TestAttachment testAttachment1 = new TestAttachment("Any content 1");
    TestAttachment testAttachment2 = new TestAttachment("Any content 2");
    AttachmentsInMemoryDatabase underTest =
        new AttachmentsInMemoryDatabase(
            HashMap.of(ANY_USER, List.of(testAttachment1, testAttachment2)));

    // when
    Try<EventAttachment<?>> result = underTest.findById(ANY_USER, testAttachment1.id());

    // then
    assertThat(result).isSuccess();
    assertEquals(result.get(), testAttachment1);
  }

  @Test
  public void shouldAddAttachmentWhenNoAttachmentsAdded() {
    // given
    TestAttachment testAttachment = new TestAttachment("Any content");
    AttachmentsInMemoryDatabase underTest = new AttachmentsInMemoryDatabase(HashMap.empty());

    // when
    Try<EventAttachment<?>> result = underTest.addAttachment(ANY_EVENT_CREATOR, testAttachment);

    // then
    assertThat(result).isSuccess();
    assertEquals(result.get(), testAttachment);

    // when
    Try<EventAttachment<?>> resultFindById = underTest.findById(ANY_USER, testAttachment.id());

    // then
    assertThat(resultFindById).isSuccess();
    assertEquals(resultFindById.get(), testAttachment);
  }

  @Test
  public void shouldAppendAttachment() {
    // given
    TestAttachment testAttachment1 = new TestAttachment("Any content 1");
    TestAttachment testAttachment2 = new TestAttachment("Any content 2");

    AttachmentsInMemoryDatabase underTest =
        new AttachmentsInMemoryDatabase(HashMap.of(ANY_USER, List.of(testAttachment1)));

    // when
    Try<EventAttachment<?>> result = underTest.addAttachment(ANY_EVENT_CREATOR, testAttachment2);

    // then
    assertThat(result).isSuccess();
    assertEquals(result.get(), testAttachment2);

    // when
    Try<EventAttachment<?>> resultFindById = underTest.findById(ANY_USER, testAttachment1.id());

    // then
    assertThat(resultFindById).isSuccess();
    assertEquals(resultFindById.get(), testAttachment1);

    // when
    resultFindById = underTest.findById(ANY_USER, testAttachment2.id());

    // then
    assertThat(resultFindById).isSuccess();
    assertEquals(resultFindById.get(), testAttachment2);
  }

  private static class TestAttachment extends EventAttachment<String> {

    public TestAttachment(String content) {
      super(EventAttachmentId.random(), content);
    }
  }
}
