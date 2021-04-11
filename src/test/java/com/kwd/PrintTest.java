package com.kwd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrintTest {

    private Writer backupWriter;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        backupWriter = Print.writer;
        Print.writer = new OutputStreamWriter(output);
    }

    @Test
    void printAboveAverageAllTime_prints_in_green_letters() throws IOException {
        Print.printAboveAverageAllTime();

        Print.writer.flush();
        assertEquals("\u001B[32mBetter than all time average!\u001B[0m\n", output.toString());
    }

    @AfterEach
    void tearDown() {
        Print.writer = backupWriter;
    }
}
