package com.kwd;

import com.kwd.thymeleaf.TypingTrackerDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Locale;
import java.util.Map;

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
        System.out.println(output);
        assertEquals("Hello, \u001B[44m\u001B[33mNerijus\u001B[0m\u001B[0m!", output);
    }
}
