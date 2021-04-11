package com.kwd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.AbstractAttributeModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.kwd.Constants.ANSI_BLACK;
import static com.kwd.Constants.ANSI_BLACK_BACKGROUND;
import static com.kwd.Constants.ANSI_BLUE;
import static com.kwd.Constants.ANSI_BLUE_BACKGROUND;
import static com.kwd.Constants.ANSI_CYAN;
import static com.kwd.Constants.ANSI_CYAN_BACKGROUND;
import static com.kwd.Constants.ANSI_GREEN;
import static com.kwd.Constants.ANSI_GREEN_BACKGROUND;
import static com.kwd.Constants.ANSI_PURPLE;
import static com.kwd.Constants.ANSI_PURPLE_BACKGROUND;
import static com.kwd.Constants.ANSI_RED;
import static com.kwd.Constants.ANSI_RED_BACKGROUND;
import static com.kwd.Constants.ANSI_RESET;
import static com.kwd.Constants.ANSI_WHITE;
import static com.kwd.Constants.ANSI_WHITE_BACKGROUND;
import static com.kwd.Constants.ANSI_YELLOW;
import static com.kwd.Constants.ANSI_YELLOW_BACKGROUND;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ThymeleafColorfulConsoleOutputTest {

    private TemplateEngine engine;
    private Context ctxWithName;

    @BeforeEach
    void setUp() {
        engine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        engine.setTemplateResolver(resolver);
        engine.addDialect(new TypingTrackerDialect());
        ctxWithName = new Context(Locale.ENGLISH, Map.of("name", "Nerijus"));
    }

    @Test
    void output_hello_template_file_from_class_loader() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setSuffix(".txt");
        engine.setTemplateResolver(resolver);

        String output = engine.process("hello-template", ctxWithName);

        assertEquals("Hello, Nerijus!\n", output);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Hello, [# tt:color=blue][[${name}]][/]!",
        "Hello, [# tt:color=blue th:text=\"${name}\" /]!",
        "Hello, [#th:block tt:color=blue th:text=\"${name}\" /]!",
    })
    void output_blue_letters(String template) {
        String output = engine.process(template, ctxWithName);

        assertEquals("Hello, \u001B[34mNerijus\u001B[0m!", output);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Hello, [# tt:bg=blue][[${name}]][/]!",
        "Hello, [# tt:bg=blue th:text=\"${name}\" /]!",
        "Hello, [#th:block tt:bg=blue th:text=\"${name}\" /]!",
    })
    void output_blue_background(String template) {
        String output = engine.process(template, ctxWithName);

        assertEquals("Hello, \u001B[44mNerijus\u001B[0m!", output);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Hello, [# tt:bg=blue tt:color=yellow][[${name}]][/]!",
        "Hello, [# tt:bg=blue tt:color=yellow th:text=\"${name}\" /]!",
        "Hello, [#th:block tt:bg=blue tt:color=yellow th:text=\"${name}\" /]!",
    })
    void output_blue_background_and_yellow_letters(String template) {
        String output = engine.process(template, ctxWithName);

        assertEquals("Hello, \u001B[44m\u001B[33mNerijus\u001B[0m\u001B[0m!", output);
    }

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

    public class ColorModelProcessor extends AbstractAttributeModelProcessor {

        private static final String ATTR_NAME = "color";
        private static final int PRECEDENCE = 100;


        public ColorModelProcessor(final String dialectPrefix) {
            super(TemplateMode.TEXT, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        }

        protected void doProcess(
            final ITemplateContext context,
            final IModel model,
            final AttributeName attrName,
            final String attrValue,
            final IElementModelStructureHandler structureHandler
        ) {
            final IModelFactory modelFactory = context.getModelFactory();

            String color = switch (attrValue) {
                case "black" -> ANSI_BLACK;
                case "red" -> ANSI_RED;
                case "green" -> ANSI_GREEN;
                case "yellow" -> ANSI_YELLOW;
                case "blue" -> ANSI_BLUE;
                case "purple" -> ANSI_PURPLE;
                case "cyan" -> ANSI_CYAN;
                case "white" -> ANSI_WHITE;
                default -> throw new TemplateProcessingException("Unexpected value: " + attrValue);
            };

            model.insert(0, modelFactory.createText(color));
            model.add(modelFactory.createText(ANSI_RESET));
        }
    }

    public class BackgroundModelProcessor extends AbstractAttributeModelProcessor {

        private static final String ATTR_NAME = "bg";
        private static final int PRECEDENCE = 100;


        public BackgroundModelProcessor(final String dialectPrefix) {
            super(TemplateMode.TEXT, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        }

        protected void doProcess(
            final ITemplateContext context,
            final IModel model,
            final AttributeName attrName,
            final String attrValue,
            final IElementModelStructureHandler structureHandler
        ) {
            final IModelFactory modelFactory = context.getModelFactory();

            String color = switch (attrValue) {
                case "black" -> ANSI_BLACK_BACKGROUND;
                case "red" -> ANSI_RED_BACKGROUND;
                case "green" -> ANSI_GREEN_BACKGROUND;
                case "yellow" -> ANSI_YELLOW_BACKGROUND;
                case "blue" -> ANSI_BLUE_BACKGROUND;
                case "purple" -> ANSI_PURPLE_BACKGROUND;
                case "cyan" -> ANSI_CYAN_BACKGROUND;
                case "white" -> ANSI_WHITE_BACKGROUND;
                default -> throw new TemplateProcessingException("Unexpected value: " + attrValue);
            };

            model.insert(0, modelFactory.createText(color));
            model.add(modelFactory.createText(ANSI_RESET));
        }
    }

}
