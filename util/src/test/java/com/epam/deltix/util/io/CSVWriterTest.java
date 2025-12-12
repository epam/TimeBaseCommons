package com.epam.deltix.util.io;

import com.epam.deltix.util.csvx.CSVXReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class CSVWriterTest {

    @Test
    void printCellWithComaDefaultSeparator() throws IOException {

        StringWriter out = new StringWriter();
        CSVWriter writer = new CSVWriter(out);
        writer.writeCells("line, with escape", "line without escape", "line with, multiple escape, characters");
        String actual = extractBuffer(out);
        String expected = "\"line, with escape\",line without escape,\"line with, multiple escape, characters\"";
        assertEquals(expected, actual);
        writer.writeCells("line\" with other escape", "line without escape", "\"line with, multiple different\" escape, characters");
        actual = extractBuffer(out);
        expected = "\"line\"\" with other escape\",line without escape,\"\"\"line with, multiple different\"\" escape, characters\"";
        assertEquals(expected, actual);
    }

    @Test
    void printCellWithPipeSeparator() throws IOException {

        StringWriter out = new StringWriter();
        CSVWriter writer = new CSVWriter(out, '|');
        writer.writeCells("line| with escape", "line without escape", "line with| multiple escape| characters");
        String actual = extractBuffer(out);
        String expected = "\"line| with escape\"|line without escape|\"line with| multiple escape| characters\"";
        assertEquals(expected, actual);
        writer.writeCells("line\" with other escape", "line with, escape coma", "\"line with, multiple |different\" escape, characters");
        actual = extractBuffer(out);
        expected = "\"line\"\" with other escape\"|line with, escape coma|\"\"\"line with, multiple |different\"\" escape, characters\"";
        assertEquals(expected, actual);
    }

    @Test
    void printCellWithPipeSeparatorWithRider() throws IOException {

        StringWriter out = new StringWriter();
        CSVWriter writer = new CSVWriter(out, '|');
        writer.writeCells("line| with escape", "line without escape", "line with| multiple escape| characters");
        CSVXReader reader = new CSVXReader(new StringReader(extractBuffer(out)), '|', false, "out");
        reader.nextLine();
        assertEquals(3, reader.getCells().length);
    }

    @Test
    void printCellWithAdditionalEscapeCharacters() throws IOException {

        StringWriter out = new StringWriter();
        CSVWriter writer = new CSVWriter(out, '|', '"', '\t');
        writer.writeCells("line| with escape", "line with additional\t\t escape", "line with no\n escape new line",
                "line with escape\" quote char");
        String actual = extractBuffer(out);
        String expected = "\"line| with escape\"|\"line with additional\t\t escape\"|line with no\n escape new line|\"line with escape\"\" quote char\"";
        assertEquals(expected, actual);
    }

    @Test
    void printCellWithAnotherQuoteCharacter() throws IOException {

        StringWriter out = new StringWriter();
        CSVWriter writer = new CSVWriter(out, '\t', '\'');
        writer.writeCells("line| with ,default\" \r\nnot used escapes", "line with \t\t separator", "line wit' quote char");
        String actual = extractBuffer(out);
        String expected = "line| with ,default\" \r\nnot used escapes\t'line with \t\t separator'\t'line wit'' quote char'";
        assertEquals(expected, actual);
    }
    @Test
    void printCellWithEscapeEOL() throws IOException {

        StringWriter out = new StringWriter();
        CSVWriter writer = new CSVWriter(out, '\t');
        writer.writeCells("line\t with separator", "line with \n new line", "line without escape", "line with \r carriage return");
        String actual = extractBuffer(out);
        String expected = "\"line\t with separator\"\t\"line with \n new line\"\tline without escape\t\"line with \r carriage return\"";
        assertEquals(expected, actual);
        CSVXReader reader = new CSVXReader(new StringReader(actual), '\t', false, "out");
        reader.nextLine();
        assertEquals(4, reader.getCells().length);
    }

    private String extractBuffer(StringWriter out) {
        String result = out.getBuffer().toString();
        out.getBuffer().setLength(0);
        return result;
    }
}