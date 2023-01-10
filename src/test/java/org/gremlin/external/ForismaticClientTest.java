package org.gremlin.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.gremlin.TestConstants;
import org.gremlin.models.Language;
import org.gremlin.models.Quote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@ExtendWith(MockitoExtension.class)
public class ForismaticClientTest {
    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse mockHttpResponse;

    private ForismaticClient client;

    @BeforeEach
    public void setup() {
        client = new ForismaticClient(mockObjectMapper, TestConstants.URL, mockHttpClient);
    }

    @Test
    @SneakyThrows
    public void testGetQuote_shouldReturnQuote_whenValidInput() {
        // Arrange
        final Quote expected = Quote.builder().quoteText(TestConstants.QUOTE_TEXT).quoteAuthor(TestConstants.QUOTE_AUTHOR).build();
        // Running out of time to work on this. For the below, we should not be using "any". Tests should use expected
        // input to avoid false passes.
        Mockito.when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockHttpResponse);
        Mockito.when(mockHttpResponse.body()).thenReturn(TestConstants.QUOTE_JSON);
        Mockito.when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class)))
                .thenReturn(expected);

        // Act
        final Quote actual = client.getQuote(Language.English.getCountryCode());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetQuote_shouldThrowRuntimeException_whenInvalidURL() {
        // Arrange
        final ForismaticClient invalidUrlClient = new ForismaticClient(mockObjectMapper, "foo", mockHttpClient);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> invalidUrlClient.getQuote(Language.English.getCountryCode()));
    }

    @Test
    @SneakyThrows
    public void testGetQuote_shouldThrowRuntimeException_whenHttpRequestFails() {
        // Arrange
        Mockito.when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenThrow(InterruptedException.class);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> client.getQuote(Language.English.getCountryCode()));
    }

    @Test
    @SneakyThrows
    public void testGetQuote_shouldThrowRuntimeException_whenJsonMappingFails() {
        // Arrange
        Mockito.when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockHttpResponse);
        Mockito.when(mockHttpResponse.body()).thenReturn(TestConstants.QUOTE_JSON);
        Mockito.when(mockObjectMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class))).thenThrow(JsonProcessingException.class);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> client.getQuote(Language.English.getCountryCode()));
    }
}
