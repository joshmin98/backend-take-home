package org.gremlin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.gremlin.commands.QuoteCommand;
import org.gremlin.modules.AppModule;
import picocli.CommandLine;

public class App {
    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new AppModule());
        final QuoteCommand quoteCommand = injector.getInstance(QuoteCommand.class);
        final int exitCode = new CommandLine(quoteCommand).execute(args);
        System.exit(exitCode);
    }
}