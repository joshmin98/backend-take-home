package org.gremlin.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.NonNull;
import org.apache.http.client.utils.URIBuilder;
import org.gremlin.models.Quote;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ForismaticClient {
    private ObjectMapper mapper;
    private String url;
    private HttpClient client;

    @Inject
    public ForismaticClient(
            final ObjectMapper mapper,
            @Named("FORISMATIC_URL") final String url,
            final HttpClient client
    ) {
        this.mapper = mapper;
        this.url = url;
        this.client = client;
    }

    public Quote getQuote(@NonNull final String language) {
        try {
            final URI uri = new URIBuilder(url)
                    .addParameter("method", "getQuote")
                    .addParameter("format", "json")
                    .addParameter("lang", language)
                    .build();
            final HttpRequest req = HttpRequest.newBuilder().GET().uri(uri).build();

            final String json = client.send(req, HttpResponse.BodyHandlers.ofString()).body();

            return mapper.readValue(json, new TypeReference<Quote>() {});
        } catch (final IOException | InterruptedException | URISyntaxException e) {
            // Just throwing a wrapped exception
            throw new RuntimeException(e);
        }
    }
}
