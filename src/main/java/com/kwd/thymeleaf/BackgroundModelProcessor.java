package com.kwd.thymeleaf;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.processor.element.AbstractAttributeModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import static com.kwd.Constants.ANSI_BLACK_BACKGROUND;
import static com.kwd.Constants.ANSI_BLUE_BACKGROUND;
import static com.kwd.Constants.ANSI_CYAN_BACKGROUND;
import static com.kwd.Constants.ANSI_GREEN_BACKGROUND;
import static com.kwd.Constants.ANSI_PURPLE_BACKGROUND;
import static com.kwd.Constants.ANSI_RED_BACKGROUND;
import static com.kwd.Constants.ANSI_RESET;
import static com.kwd.Constants.ANSI_WHITE_BACKGROUND;
import static com.kwd.Constants.ANSI_YELLOW_BACKGROUND;

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
