package org.gremlin.commands;

import com.google.inject.Inject;
import org.gremlin.constants.CommandConstants;
import org.gremlin.external.ForismaticClient;
import org.gremlin.models.Language;
import org.gremlin.models.Quote;
import picocli.CommandLine;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@CommandLine.Command(name = "Quote", description = "Gets quote from Forismatic API")
public class QuoteCommand implements Runnable {
    @Inject
    private ForismaticClient client;

    @CommandLine.Option(
            names = {"-l", "--language"},
            description = "Quote language, one of: ${COMPLETION-CANDIDATES}",
            required = true
    )
    private Language language;
    @Override
    public void run() {
        try {
            final PrintStream output = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            final Quote quote = client.getQuote(language.getCountryCode());
            output.printf(CommandConstants.QUOTE_MESSAGE_FORMAT, quote.getQuoteText(), quote.getQuoteAuthor());
        } catch (final Exception e) {
            System.out.printf(CommandConstants.QUOTE_EXCEPTION_MESSAGE_FORMAT, e.getMessage());
        }
    }
}
