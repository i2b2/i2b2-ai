package edu.harvard.i2b2.crc.opencsv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.harvard.i2b2.crc.opencsv.stream.reader.LineReader;

/**
 * A very simple CSV reader released under a commercial-friendly license.
 *
 * @author Glen Smith
 */
public class CSVReader implements Closeable, Iterable<String[]> {
    public static final boolean DEFAULT_KEEP_CR = false;
    public static final boolean DEFAULT_VERIFY_READER = true;
    /**
     * The default line to start reading.
     */
    public static final int DEFAULT_SKIP_LINES = 0;
    public static final int READ_AHEAD_LIMIT = Character.SIZE / Byte.SIZE;
    public char seprator;
    private CSVParser parser;
    private int skipLines;
    private BufferedReader br;
    private LineReader lineReader;
    private boolean hasNext = true;
    private boolean linesSkiped;
    private boolean keepCR;
    private boolean verifyReader;
    private long linesRead = 0;
    private long recordsRead = 0;

    /**
     * Constructs CSVReader using a comma for the separator.
     *
     * @param reader the reader to an underlying CSV source.
     */
    public CSVReader(Reader reader) {
        this(reader, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries.
     */
    public CSVReader(Reader reader, char separator) {
        this(reader, separator, CSVParser.DEFAULT_QUOTE_CHARACTER, CSVParser.DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator and quote char.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quotechar the character to use for quoted elements
     */
    public CSVReader(Reader reader, char separator, char quotechar) {
        this(reader, separator, quotechar, CSVParser.DEFAULT_ESCAPE_CHARACTER, DEFAULT_SKIP_LINES, CSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader with supplied separator, quote char and quote handling
     * behavior.
     *
     * @param reader       the reader to an underlying CSV source.
     * @param separator    the delimiter to use for separating entries
     * @param quotechar    the character to use for quoted elements
     * @param strictQuotes sets if characters outside the quotes are ignored
     */
    public CSVReader(Reader reader, char separator, char quotechar, boolean strictQuotes) {
        this(reader, separator, quotechar, CSVParser.DEFAULT_ESCAPE_CHARACTER, DEFAULT_SKIP_LINES, strictQuotes);
    }

    /**
     * Constructs CSVReader.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quotechar the character to use for quoted elements
     * @param escape    the character to use for escaping a separator or quote
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape) {
        this(reader, separator, quotechar, escape, DEFAULT_SKIP_LINES, CSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quotechar the character to use for quoted elements
     * @param line      the line number to skip for start reading
     */
    public CSVReader(Reader reader, char separator, char quotechar, int line) {
        this(reader, separator, quotechar, CSVParser.DEFAULT_ESCAPE_CHARACTER, line, CSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quotechar the character to use for quoted elements
     * @param escape    the character to use for escaping a separator or quote
     * @param line      the line number to skip for start reading
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape, int line) {
        this(reader, separator, quotechar, escape, line, CSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader.
     *
     * @param reader       the reader to an underlying CSV source.
     * @param separator    the delimiter to use for separating entries
     * @param quotechar    the character to use for quoted elements
     * @param escape       the character to use for escaping a separator or quote
     * @param line         the line number to skip for start reading
     * @param strictQuotes sets if characters outside the quotes are ignored
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes) {
        this(reader, separator, quotechar, escape, line, strictQuotes, CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
    }

    /**
     * Constructs CSVReader with all data entered.
     *
     * @param reader                  the reader to an underlying CSV source.
     * @param separator               the delimiter to use for separating entries
     * @param quotechar               the character to use for quoted elements
     * @param escape                  the character to use for escaping a separator or quote
     * @param line                    the line number to skip for start reading
     * @param strictQuotes            sets if characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace it true, parser should ignore white space before a quote in a field
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes, boolean ignoreLeadingWhiteSpace) {
        this(reader, line, new CSVParser(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace));
    }

    /**
     * Constructs CSVReader with all data entered.
     *
     * @param reader                  the reader to an underlying CSV source.
     * @param separator               the delimiter to use for separating entries
     * @param quotechar               the character to use for quoted elements
     * @param escape                  the character to use for escaping a separator or quote
     * @param line                    the line number to skip for start reading
     * @param strictQuotes            sets if characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace if true, parser should ignore white space before a quote in a field
     * @param keepCR                  if true the reader will keep carriage returns, otherwise it will discard them.
     */
    public CSVReader(Reader reader, char separator, char quotechar, char escape, int line, boolean strictQuotes, boolean ignoreLeadingWhiteSpace, boolean keepCR) {
        this(reader, line, new CSVParser(separator, quotechar, escape, strictQuotes, ignoreLeadingWhiteSpace), keepCR, DEFAULT_VERIFY_READER);
    }

    /**
     * Constructs CSVReader with supplied CSVParser.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param line      the line number to skip for start reading
     * @param csvParser the parser to use to parse input
     */
    public CSVReader(Reader reader, int line, CSVParser csvParser) {
        this(reader, line, csvParser, DEFAULT_KEEP_CR, DEFAULT_VERIFY_READER);
    }

    /**
     * Constructs CSVReader with supplied CSVParser.
     *
     * @param reader       the reader to an underlying CSV source.
     * @param line         the line number to skip for start reading
     * @param csvParser    the parser to use to parse input
     * @param keepCR       true to keep carriage returns in data read, false otherwise
     * @param verifyReader true to verify reader before each read, false otherwise
     */
    CSVReader(Reader reader, int line, CSVParser csvParser, boolean keepCR, boolean verifyReader) {
        this.br = (reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader));
        this.lineReader = new LineReader(br, keepCR);
        this.skipLines = line;
        this.parser = csvParser;
        this.keepCR = keepCR;
        this.seprator = csvParser.getSeparator();
        this.verifyReader = verifyReader;
    }

    /**
     * @return the CSVParser used by the reader.
     */
    public CSVParser getParser() {
        return parser;
    }

    /**
     * Returns the number of lines in the csv file to skip before processing.  This is
     * useful when there is miscellaneous data at the beginning of a file.
     *
     * @return the number of lines in the csv file to skip before processing.
     */
    public int getSkipLines() {
        return skipLines;
    }

    /**
     * Returns if the reader will keep carriage returns found in data or remove them.
     *
     * @return true if reader will keep carriage returns, false otherwise.
     */
    public boolean keepCarriageReturns() {
        return keepCR;
    }

    /**
     * Reads the entire file into a List with each element being a String[] of
     * tokens.
     *
     * @return a List of String[], with each String[] representing a line of the
     * file.
     * @throws IOException if bad things happen during the read
     */
    public List<String[]> readAll() throws IOException {

        List<String[]> allElements = new ArrayList<String[]>();
        while (hasNext) {
            String[] nextLineAsTokens = readNext();
            if (nextLineAsTokens != null) {
                allElements.add(nextLineAsTokens);
            }
        }
        return allElements;
    }

    /**
     * Reads the next line from the buffer and converts to a string array.
     *
     * @return a string array with each comma-separated element as a separate
     * entry.
     * @throws IOException if bad things happen during the read
     */
    public String[] readNext() throws IOException {

        String[] result = null;
        do {
            String nextLine = getNextLine();
            if (!hasNext) {
                return validateResult(result);
            }
            String[] r = parser.parseLineMulti(nextLine);
            if (r.length > 0) {
                if (result == null) {
                    result = r;
                } else {
                    result = combineResultsFromMultipleReads(result, r);
                }
            }
        } while (parser.isPending());
        return validateResult(result);
    }

    private String[] validateResult(String[] result) {
        if (result != null) {
            recordsRead++;
        }
        return result;
    }

    /**
     * For multi line records this method combines the current result with the result from previous read(s).
     *
     * @param buffer   - previous data read for this record
     * @param lastRead - latest data read for this record.
     * @return String array with union of the buffer and lastRead arrays.
     */
    private String[] combineResultsFromMultipleReads(String[] buffer, String[] lastRead) {
        String[] t = new String[buffer.length + lastRead.length];
        System.arraycopy(buffer, 0, t, 0, buffer.length);
        System.arraycopy(lastRead, 0, t, buffer.length, lastRead.length);
        return t;
    }

    /**
     * Reads the next line from the file.
     *
     * @return the next line from the file without trailing newline
     * @throws IOException if bad things happen during the read
     */
    private String getNextLine() throws IOException {
        if (isClosed()) {
            hasNext = false;
            return null;
        }

        if (!this.linesSkiped) {
            for (int i = 0; i < skipLines; i++) {
                lineReader.readLine();
                linesRead++;
            }
            this.linesSkiped = true;
        }
        String nextLine = lineReader.readLine();
        if (nextLine == null) {
            hasNext = false;
        } else {
            linesRead++;
        }

        return hasNext ? nextLine : null;
    }

    /**
     * Checks to see if the file is closed.
     *
     * @return true if the reader can no longer be read from.
     */
    private boolean isClosed() {
        if (!verifyReader) {
            return false;
        }
        try {
            br.mark(READ_AHEAD_LIMIT);
            int nextByte = br.read();
            br.reset(); // resets stream position, possible because its buffered
            return nextByte == -1; // read() returns -1 at end of stream
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * Closes the underlying reader.
     *
     * @throws IOException if the close fails
     */
    public void close() throws IOException {
        br.close();
    }

    /**
     * Creates an Iterator for processing the csv data.
     *
     * @return an String[] iterator.
     */
    public Iterator<String[]> iterator() {
        try {
            return new CSVIterator(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns if the CSVReader will verify the reader before each read.
     * <p/>
     * By default the value is true which is the functionality for version 3.0.
     * If set to false the reader is always assumed ready to read - this is the functionality
     * for version 2.4 and before.
     * <p/>
     * The reason this method was needed was that certain types of Readers would return
     * false for its ready() method until a read was done (namely readers created using Channels).
     * This caused opencsv not to read from those readers.
     *
     * @return true if CSVReader will verify the reader before reads.  False otherwise.
     * @link https://sourceforge.net/p/opencsv/bugs/108/
     * @since 3.3
     */
    public boolean verifyReader() {
        return this.verifyReader;
    }

    /**
     * Used for debugging purposes this method returns the number of lines that has been read from
     * the reader passed into the CSVReader.
     * <p/>
     * Given the following data.
     * <code>
     * <pre>
     * First line in the file
     * some other descriptive line
     * a,b,c
     *
     * a,"b\nb",c
     * </pre>
     * </code>
     * With a CSVReader constructed like so
     * <code>
     * <pre>
     * CSVReader c = builder.withCSVParser(new CSVParser())
     *                      .withSkipLines(2)
     *                      .build();
     * </pre>
     * </code>
     * The initial call to getLinesRead will be 0.<br>
     * After the first call to readNext() then getLinesRead will return 3 (because header was read).<br>
     * After the second call to read the blank line then getLinesRead will return 4 (still a read).<br>
     * After third call to readNext getLinesRead will return 6 because it took two line reads to retrieve this record.<br>
     * Subsequent calls to readNext (since we are out of data) will not increment the number of lines read.<br>
     * <p/>
     * An example of this is in the linesAndRecordsRead() test in CSVReaderTest.
     *
     * @return the number of lines read by the reader (including skip lines).
     * @link https://sourceforge.net/p/opencsv/feature-requests/73/
     * @since 3.6
     */
    public long getLinesRead() {
        return linesRead;
    }

    /**
     * Used for debugging purposes this method returns the number of records that has been read from
     * the CSVReader.
     * <p/>
     * Given the following data.
     * <code><pre>
     * First line in the file
     * some other descriptive line
     * a,b,c
     * <p/>
     * a,"b\nb",c
     * </pre></code>
     * With a CSVReader constructed like so
     * <code><pre>
     * CSVReader c = builder.withCSVParser(new CSVParser())
     *                      .withSkipLines(2)
     *                      .build();
     * </pre></code>
     * The initial call to getRecordsRead will be 0.<br>
     * After the first call to readNext() then getRecordsRead will return 1.<br>
     * After the second call to read the blank line then getRecordsRead will return 2
     * (a blank line is considered a record with one empty field).<br>
     * After third call to readNext getRecordsRead will return 3 because even though
     * reads to retrieve this record it is still a single record read.<br>
     * Subsequent calls to readNext (since we are out of data) will not increment the number of records read.<br>
     * <p/>
     * An example of this is in the linesAndRecordsRead() test in CSVReaderTest.
     *
     * @return the number of records (Array of Strings[]) read by the reader.
     * @link https://sourceforge.net/p/opencsv/feature-requests/73/
     * @since 3.6
     */
    public long getRecordsRead() {
        return recordsRead;
    }
}
