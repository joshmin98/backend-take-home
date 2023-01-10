package org.gremlin.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.gremlin.commands.QuoteCommand;
import org.gremlin.external.ForismaticClient;

import java.net.http.HttpClient;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("FORISMATIC_URL"))
                .toInstance("https://api.forismatic.com/api/1.0/");
        bind(ForismaticClient.class).in(Singleton.class);
        bind(QuoteCommand.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    HttpClient getHttpClient() {
        return HttpClient.newHttpClient();
    }

    @Provides
    @Singleton
    ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
