package com.kwd;

import com.kwd.thymeleaf.TypingTrackerDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.kwd.Constants.*;

public class Print {

    private static final TemplateEngine engine;
    static Writer writer = new OutputStreamWriter(System.out);

    static {
        engine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        engine.setTemplateResolver(resolver);
        engine.addDialect(new TypingTrackerDialect());
    }

    private Print() {
    }

    static void printAboveAverageAllTime() {
        engine.process("[# tt:color=green]Better than all time average![/]\n", new Context(), writer);
    }

    static void printAboveAveragePast7Days() {
        System.out.println(Constants.ANSI_GREEN + "Better than average for last 7 days! " + Constants.ANSI_RESET);
    }

    static void printAboveAveragePast3Days() {
        System.out.println(Constants.ANSI_GREEN + "Better than average for last 3 days! " + Constants.ANSI_RESET);
    }

    static void printTiedPersonalBest() {
        System.out.println(Constants.ANSI_CYAN + "You have TIED your personal best!!!" + Constants.ANSI_RESET);
    }

    static void printNewPersonalBest() {
        System.out.println(Constants.ANSI_CYAN + ">>>> New personal best! Celebration time! <<<<" + Constants.ANSI_RESET);
    }

    static void printEncouragement() {
        System.out.println("Stick with it and you will get better!");
    }

    static void printMapStatistics(HashMap<String, Statistic> map) {
        printStatistic(map.get(FIRST_7_DAYS));
        printStatistic(map.get(ALL_TIME));
        printStatistic(map.get(LAST_7_DAYS));
        printStatistic(map.get(LAST_3_DAYS));
    }

    static void printMapStatisticsComparison(HashMap<String, Statistic> before, HashMap<String, Statistic> after) {
        printStatisticComparison(before.get(FIRST_7_DAYS), after.get(FIRST_7_DAYS));
        printStatisticComparison(before.get(ALL_TIME), after.get(ALL_TIME));
        printStatisticComparison(before.get(LAST_7_DAYS), after.get(LAST_7_DAYS));
        printStatisticComparison(before.get(LAST_3_DAYS), after.get(LAST_3_DAYS));

    }

    private static void printStatisticComparison(Statistic before, Statistic after) {
        Float errorDelta = after.getErrorRate() - before.getErrorRate();
        String errorDeltaColor = ANSI_RESET;
        if (errorDelta < 0) {
            errorDeltaColor = ANSI_GREEN;
        }
        if (errorDelta > 0) {
            errorDeltaColor = ANSI_RED;
        }
        Float wpmDelta = after.getWpm() - before.getWpm();
        String wpmDeltaColor = ANSI_RESET;
        if (wpmDelta > 0) {
            wpmDeltaColor = ANSI_GREEN;
        }
        if (wpmDelta < 0) {
            wpmDeltaColor = ANSI_RED;
        }
        System.out.printf("%" + CATEGORY_STRING_MAX_LENGTH + "s:"
                        + "%" + ERROR_RATE_DIGITS + ".2f"
                        + "(%s %+" + (ERROR_RATE_DIGITS - 1) + ".2f%s)%%   "
                        + "%" + WPM_DIGITS + ".2f"
                        + "(%s%+" + (WPM_DIGITS - 2) + ".2f%s)wpm"
                        + "  %s --> %s\n"
                , after.getName(),
                after.getErrorRate(),
                errorDeltaColor, errorDelta, ANSI_RESET,
                after.getWpm(),
                wpmDeltaColor, wpmDelta, ANSI_RESET,
                dateFormatter.format(after.getStartDate()), dateTimeFormatter.format(after.getEndDate())
        );
    }

    private static void printStatistic(Statistic statistic) {
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%"
                        + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm  %s --> %s\n"
                , statistic.getName(), statistic.getErrorRate(), statistic.getWpm(),
                dateFormatter.format(statistic.getStartDate()), dateTimeFormatter.format(statistic.getEndDate())
        );
    }

    static void printBest(Map<String, Statistic> map) {
        String personalBest = "Personal best";
        System.out.printf("%" + Constants.CATEGORY_STRING_MAX_LENGTH + "s:%" + Constants.ERROR_RATE_DIGITS + ".2f%% %" + Constants.WPM_DIGITS + ".2fwpm  %s\n"
                , personalBest, map.get(BEST).getErrorRate(), map.get(BEST).getWpm(), dateTimeFormatter.format(map.get(BEST).getStartDate()));
    }

}
