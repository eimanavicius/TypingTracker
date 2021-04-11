package com.kwd.thymeleaf;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.processor.element.AbstractAttributeModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import static com.kwd.Constants.ANSI_BLACK;
import static com.kwd.Constants.ANSI_BLUE;
import static com.kwd.Constants.ANSI_CYAN;
import static com.kwd.Constants.ANSI_GREEN;
import static com.kwd.Constants.ANSI_PURPLE;
import static com.kwd.Constants.ANSI_RED;
import static com.kwd.Constants.ANSI_RESET;
import static com.kwd.Constants.ANSI_WHITE;
import static com.kwd.Constants.ANSI_YELLOW;

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
