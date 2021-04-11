package com.kwd.thymeleaf;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import java.util.Set;

public class TypingTrackerDialect extends AbstractProcessorDialect {

    private static final String DIALECT_NAME = "TypingTracker Dialect";

    public TypingTrackerDialect() {
        super(DIALECT_NAME, "tt", StandardDialect.PROCESSOR_PRECEDENCE);
    }

    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        return Set.of(
            new ColorModelProcessor(dialectPrefix),
            new BackgroundModelProcessor(dialectPrefix)
        );
    }
}
