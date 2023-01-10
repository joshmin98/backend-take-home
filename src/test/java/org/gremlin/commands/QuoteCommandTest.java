package org.gremlin.commands;

import lombok.SneakyThrows;
import org.gremlin.TestConstants;
import org.gremlin.constants.CommandConstants;
import org.gremlin.external.ForismaticClient;
import org.gremlin.models.Language;
import org.gremlin.models.Quote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

@ExtendWith(MockitoExtension.class)
public final class QuoteCommandTest {
    @Mock
    private ForismaticClient mockClient;

    @InjectMocks
    private QuoteCommand mockQuoteCommand;

    @Test
    @SneakyThrows
    public void testRun_shouldReturnEnglishQuote_whenValidEnglishRequestSent() {
        // Arrange
        Mockito.when(mockClient.getQuote(Language.English.getCountryCode()))
                .thenReturn(Quote.builder().quoteAuthor(TestConstants.QUOTE_AUTHOR).quoteText(TestConstants.QUOTE_TEXT).build());

        // Act
        final StringWriter output = new StringWriter();
        final CommandLine cmd = new CommandLine(mockQuoteCommand);
        cmd.setOut(new PrintWriter(output));
        cmd.execute("-l=English");

        // Assert
        Assertions.assertEquals(String.format(CommandConstants.QUOTE_MESSAGE_FORMAT, TestConstants.QUOTE_TEXT, TestConstants.QUOTE_AUTHOR), output.toString());
    }

    @Test
    @SneakyThrows
    public void testRun_shouldReturnEnglishQuote_whenValidRussianRequestSent() {
        // Arrange
        Mockito.when(mockClient.getQuote(Language.Russian.getCountryCode()))
                .thenReturn(Quote.builder().quoteAuthor(TestConstants.QUOTE_AUTHOR).quoteText(TestConstants.QUOTE_TEXT).build());

        // Act
        final StringWriter output = new StringWriter();
        final CommandLine cmd = new CommandLine(mockQuoteCommand);
        cmd.setOut(new PrintWriter(output));
        cmd.execute("-l=Russian");

        // Assert
        Assertions.assertEquals(String.format(CommandConstants.QUOTE_MESSAGE_FORMAT, TestConstants.QUOTE_TEXT, TestConstants.QUOTE_AUTHOR), output.toString());
    }

    @Test
    @SneakyThrows
    public void testRun_shouldReturnErrorMessage_whenRequestFails() {
        // Arrange
        Mockito.when(mockClient.getQuote(Mockito.anyString()))
                .thenThrow(new RuntimeException(TestConstants.EXCEPTION_MESSAGE));

        // Act
        final StringWriter output = new StringWriter();
        final CommandLine cmd = new CommandLine(mockQuoteCommand);
        cmd.setOut(new PrintWriter(output));
        cmd.execute("-l=English");

        // Assert
        Assertions.assertEquals(String.format(CommandConstants.QUOTE_EXCEPTION_MESSAGE_FORMAT, TestConstants.EXCEPTION_MESSAGE), output.toString());
    }
}
