package com.bookkeeper.app.adapter.in.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bookkeeper.app.application.domain.model.EventAttachmentId;
import com.bookkeeper.app.application.port.in.FindAttachmentUseCase;
import com.bookkeeper.app.common.Anys;
import com.bookkeeper.app.common.TestSecurityConfiguration;
import io.vavr.control.Try;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@Import({TestSecurityConfiguration.class})
@WebMvcTest(AttachmentController.class)
@WithUserDetails(Anys.ANY_EMAIL)
public class AttachmentControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private FindAttachmentUseCase findAttachmentUseCase;

  @Test
  public void shouldReturnInternalServerErrorOnNonUUIDId() throws Exception {

    // given
    when(findAttachmentUseCase.findAttachment(any(), any())).thenReturn(Try.success(null));

    // when & then
    this.mockMvc
        .perform(get("/attachment/any-attachment"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldReturnNotFoundOnNonExistentAttachmentId() throws Exception {

    // given
    when(findAttachmentUseCase.findAttachment(any(), any()))
        .thenReturn(Try.failure(new RuntimeException("Attachment not found")));

    // when & then
    this.mockMvc
        .perform(get("/attachment/" + UUID.randomUUID()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Attachment not found")));
  }

  @Test
  public void shouldReturnExistingAttachment() throws Exception {

    // given
    String fileContent = "any-test-file";
    File testFile = getTestFile(fileContent);

    EventAttachmentId eventAttachmentId = EventAttachmentId.random();
    when(findAttachmentUseCase.findAttachment(any(), any()))
        .thenReturn(
            Try.success(
                FindAttachmentUseCase.Attachment.builder()
                    .attachmentId(eventAttachmentId)
                    .content(testFile)
                    .build()));

    // when & then
    this.mockMvc
        .perform(get("/attachment/" + eventAttachmentId.value()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(fileContent)));
  }

  private File getTestFile(String content) throws IOException {
    File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".txt");
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
      IOUtils.copy(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), out);
    }

    return tempFile;
  }
}
