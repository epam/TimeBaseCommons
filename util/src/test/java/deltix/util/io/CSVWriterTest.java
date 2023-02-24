package deltix.util.io;

import deltix.util.csvx.CSVXReader;
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
        expected = "\"line\"\" with other escape\"|\"line with, escape coma\"|\"\"\"line with, multiple |different\"\" escape, characters\"";
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
        CSVWriter writer = new CSVWriter(out, '|', '\r', '\n');
        writer.writeCells("line| with escape", "line with additional\r\n escape", "line without escape");
        String actual = extractBuffer(out);
        String expected = "\"line| with escape\"|\"line with additional\r\n escape\"|line without escape";
        assertEquals(expected, actual);
        CSVXReader reader = new CSVXReader(new StringReader(actual), '|', false, "out");
        reader.nextLine();
        assertEquals(3, reader.getCells().length);
    }

    @Test
    void printCellWithNoEscapeEOL() throws IOException {

        StringWriter out = new StringWriter();
        CSVWriter writer = new CSVWriter(out, '|');
        writer.writeCells("line| with escape", "line with additional\r\n escape", "line without escape");
        String actual = extractBuffer(out);
        CSVXReader reader = new CSVXReader(new StringReader(actual), '|', false, "out");
        reader.nextLine();
        assertNotEquals(3, reader.getCells().length);
    }

    private String extractBuffer(StringWriter out) {
        String result = out.getBuffer().toString();
        out.getBuffer().setLength(0);
        return result;
    }
}