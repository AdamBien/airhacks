package org.json;

import java.io.*;
import java.nio.charset.StandardCharsets;

/*
Public Domain.
 */

/**
 * A JSONTokener takes a source string and extracts characters and tokens from
 * it. It is used by the JSONObject and JSONArray constructors to parse
 * JSON source strings.
 * @author JSON.org
 * @version 2014-05-03
 */
public class JSONTokener {
    /** current read character position on the current line. */
    private long character;
    /** flag to indicate if the end of the input has been found. */
    private boolean eof;
    /** current read index of the input. */
    private long index;
    /** current line of the input. */
    private long line;
    /** previous character read from the input. */
    private char previous;
    /** Reader for the input. */
    private final Reader reader;
    /** flag to indicate that a previous character was requested. */
    private boolean usePrevious;
    /** the number of characters read in the previous line. */
    private long characterPreviousLine;

    // access to this object is required for strict mode checking
    private JSONParserConfiguration jsonParserConfiguration;

    /**
     * Construct a JSONTokener from a Reader. The caller must close the Reader.
     *
     * @param reader the source.
     */
    public JSONTokener(Reader reader) {
        this(reader, new JSONParserConfiguration());
    }

    /**
     * Construct a JSONTokener from a Reader with a given JSONParserConfiguration. The caller must close the Reader.
     *
     * @param reader the source.
     * @param jsonParserConfiguration A JSONParserConfiguration instance that controls the behavior of the parser.
     *
     */
    public JSONTokener(Reader reader, JSONParserConfiguration jsonParserConfiguration) {
        this.jsonParserConfiguration = jsonParserConfiguration;
        this.reader = reader.markSupported()
                ? reader
                        : new BufferedReader(reader);
        this.eof = false;
        this.usePrevious = false;
        this.previous = 0;
        this.index = 0;
        this.character = 1;
        this.characterPreviousLine = 0;
        this.line = 1;
    }

    /**
     * Construct a JSONTokener from an InputStream. The caller must close the input stream.
     * @param inputStream The source.
     */
    public JSONTokener(InputStream inputStream) {
        this(inputStream, new JSONParserConfiguration());
    }

    /**
     * Construct a JSONTokener from an InputStream. The caller must close the input stream.
     * @param inputStream The source.
     * @param jsonParserConfiguration A JSONParserConfiguration instance that controls the behavior of the parser.
     */
    public JSONTokener(InputStream inputStream, JSONParserConfiguration jsonParserConfiguration) {
        this(new InputStreamReader(inputStream, StandardCharsets.UTF_8), jsonParserConfiguration);
    }


    /**
     * Construct a JSONTokener from a string.
     *
     * @param source A source string.
     */
    public JSONTokener(String source) {
        this(new StringReader(source));
    }

    /**
     * Construct a JSONTokener from an InputStream. The caller must close the input stream.
     * @param source The source.
     * @param jsonParserConfiguration A JSONParserConfiguration instance that controls the behavior of the parser.
     */
    public JSONTokener(String source, JSONParserConfiguration jsonParserConfiguration) {
        this(new StringReader(source), jsonParserConfiguration);
    }

    /**
     * Getter
     * @return jsonParserConfiguration
     */
    public JSONParserConfiguration getJsonParserConfiguration() {
        return jsonParserConfiguration;
    }

    /**
     * Setter
     * @param jsonParserConfiguration new value for jsonParserConfiguration
     *
     * @deprecated method should not be used
     */
    @Deprecated
    public void setJsonParserConfiguration(JSONParserConfiguration jsonParserConfiguration) {
        this.jsonParserConfiguration = jsonParserConfiguration;
    }

    /**
     * Back up one character. This provides a sort of lookahead capability,
     * so that you can test for a digit or letter before attempting to parse
     * the next number or identifier.
     */
    public void back() {
        if (this.usePrevious || this.index <= 0) {
            throw new IllegalStateException("Stepping back two steps is not supported");
        }
        this.decrementIndexes();
        this.usePrevious = true;
        this.eof = false;
    }

    /**
     * Decrements the indexes for the {@link #back()} method based on the previous character read.
     */
    private void decrementIndexes() {
        this.index--;
        if(this.previous=='\r' || this.previous == '\n') {
            this.line--;
            this.character=this.characterPreviousLine ;
        } else if(this.character > 0){
            this.character--;
        }
    }

    /**
     * Get the hex value of a character (base16).
     * @param c A character between '0' and '9' or between 'A' and 'F' or
     * between 'a' and 'f'.
     * @return  An int between 0 and 15, or -1 if c was not a hex digit.
     */
    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - ('A' - 10);
        }
        if (c >= 'a' && c <= 'f') {
            return c - ('a' - 10);
        }
        return -1;
    }

    /**
     * Checks if the end of the input has been reached.
     *
     * @return true if at the end of the file and we didn't step back
     */
    public boolean end() {
        return this.eof && !this.usePrevious;
    }


    /**
     * Determine if the source string still contains characters that next()
     * can consume.
     * @return true if not yet at the end of the source.
     */
    public boolean more() {
        if(this.usePrevious) {
            return true;
        }
        try {
            this.reader.mark(1);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to preserve stream position", e);
        }
        try {
            // -1 is EOF, but next() can not consume the null character '\0'
            if(this.reader.read() <= 0) {
                this.eof = true;
                return false;
            }
            this.reader.reset();
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read the next character from the stream", e);
        }
        return true;
    }


    /**
     * Get the next character in the source string.
     *
     * @return The next character, or 0 if past the end of the source string.
     */
    public char next() {
        int c;
        if (this.usePrevious) {
            this.usePrevious = false;
            c = this.previous;
        } else {
            try {
                c = this.reader.read();
            } catch (IOException exception) {
                throw new UncheckedIOException(exception);
            }
        }
        if (c <= 0) { // End of stream
            this.eof = true;
            return 0;
        }
        this.incrementIndexes(c);
        this.previous = (char) c;
        return this.previous;
    }

    /**
     * Get the last character read from the input or '\0' if nothing has been read yet.
     * @return the last character read from the input.
     */
    protected char getPrevious() { return this.previous;}

    /**
     * Increments the internal indexes according to the previous character
     * read and the character passed as the current character.
     * @param c the current character read.
     */
    private void incrementIndexes(int c) {
        if(c > 0) {
            this.index++;
            if(c=='\r') {
                this.line++;
                this.characterPreviousLine = this.character;
                this.character=0;
            }else if (c=='\n') {
                if(this.previous != '\r') {
                    this.line++;
                    this.characterPreviousLine = this.character;
                }
                this.character=0;
            } else {
                this.character++;
            }
        }
    }

    /**
     * Consume the next character, and check that it matches a specified
     * character.
     * @param c The character to match.
     * @return The character.
     */
    public char next(char c) {
        char n = this.next();
        if (n != c) {
            if(n > 0) {
                throw this.syntaxError("Expected '" + c + "' and instead saw '" +
                        n + "'");
            }
            throw this.syntaxError("Expected '" + c + "' and instead saw ''");
        }
        return n;
    }


    /**
     * Get the next n characters.
     *
     * @param n     The number of characters to take.
     * @return      A string of n characters.
     */
    public String next(int n) {
        if (n == 0) {
            return "";
        }

        char[] chars = new char[n];
        int pos = 0;

        while (pos < n) {
            chars[pos] = this.next();
            if (this.end()) {
                throw this.syntaxError("Substring bounds error");
            }
            pos += 1;
        }
        return new String(chars);
    }


    /**
     * Get the next char in the string, skipping whitespace.
     * @return  A character, or 0 if there are no more characters.
     */
    public char nextClean() {
        for (;;) {
            char c = this.next();
            if (c == 0 || c > ' ') {
                return c;
            }
        }
    }


    /**
     * Return the characters up to the next close quote character.
     * Backslash processing is done. The formal JSON format does not
     * allow strings in single quotes, but an implementation is allowed to
     * accept them.
     * @param quote The quoting character, either
     *      <code>"</code>&nbsp;<small>(double quote)</small> or
     *      <code>'</code>&nbsp;<small>(single quote)</small>.
     * @return      A String.
     */
    public String nextString(char quote) {
        char c;
        var sb = new StringBuilder();
        for (;;) {
            c = this.next();
            switch (c) {
                case 0, '\n', '\r' -> throw this.syntaxError("Unterminated string. " +
                        "Character with int code " + (int) c + " is not allowed within a quoted string.");
                case '\\' -> {
                    c = this.next();
                    switch (c) {
                        case 'b' -> sb.append('\b');
                        case 't' -> sb.append('\t');
                        case 'n' -> sb.append('\n');
                        case 'f' -> sb.append('\f');
                        case 'r' -> sb.append('\r');
                        case 'u' -> {
                            String next = this.next(4);
                            try {
                                sb.append((char) Integer.parseInt(next, 16));
                            } catch (NumberFormatException e) {
                                throw this.syntaxError("Illegal escape. " +
                                        "\\u must be followed by a 4 digit hexadecimal number. \\" + next + " is not valid.", e);
                            }
                        }
                        case '"', '\'', '\\', '/' -> sb.append(c);
                        default -> throw this.syntaxError("Illegal escape. Escape sequence  \\" + c + " is not valid.");
                    }
                }
                default -> {
                    if (c == quote) {
                        return sb.toString();
                    }
                    sb.append(c);
                }
            }
        }
    }


    /**
     * Get the text up but not including the specified character or the
     * end of line, whichever comes first.
     * @param  delimiter A delimiter character.
     * @return   A string.
     */
    public String nextTo(char delimiter) {
        var sb = new StringBuilder();
        for (;;) {
            char c = this.next();
            if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }


    /**
     * Get the text up but not including one of the specified delimiter
     * characters or the end of line, whichever comes first.
     * @param delimiters A set of delimiter characters.
     * @return A string, trimmed.
     */
    public String nextTo(String delimiters) {
        char c;
        var sb = new StringBuilder();
        for (;;) {
            c = this.next();
            if (delimiters.indexOf(c) >= 0 || c == 0 ||
                    c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }


    /**
     * Get the next value. The value can be a Boolean, Double, Integer,
     * JSONArray, JSONObject, Long, or String, or the JSONObject.NULL object.
     *
     * @return An object.
     */
    public Object nextValue() {
        char c = this.nextClean();
        switch (c) {
            case '{' -> {
                this.back();
                try {
                    return new JSONObject(this, jsonParserConfiguration);
                } catch (StackOverflowError e) {
                    throw new IllegalStateException("JSON Array or Object depth too large to process.", e);
                }
            }
            case '[' -> {
                this.back();
                try {
                    return new JSONArray(this, jsonParserConfiguration);
                } catch (StackOverflowError e) {
                    throw new IllegalStateException("JSON Array or Object depth too large to process.", e);
                }
            }
            default -> {
                // fall through to nextSimpleValue
            }
        }
        return nextSimpleValue(c);
    }

    Object nextSimpleValue(char c) {
        String string;

        // Strict mode only allows strings with explicit double quotes
        if (jsonParserConfiguration != null &&
                jsonParserConfiguration.isStrictMode() &&
                c == '\'') {
            throw this.syntaxError("Strict mode error: Single quoted strings are not allowed");
        }
        switch (c) {
            case '"', '\'' -> {
                return this.nextString(c);
            }
            default -> {
                // fall through to unquoted text handling
            }
        }

        /*
         * Handle unquoted text. This could be the values true, false, or
         * null, or it can be a number. An implementation (such as this one)
         * is allowed to also accept non-standard forms.
         *
         * Accumulate characters until we reach the end of the text or a
         * formatting character.
         */

        var sb = new StringBuilder();
        while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
            sb.append(c);
            c = this.next();
        }
        if (!this.eof) {
            this.back();
        }

        string = sb.toString().trim();
        if ("".equals(string)) {
            throw this.syntaxError("Missing value");
        }  else if (jsonParserConfiguration != null &&
                jsonParserConfiguration.isStrictMode() && string.endsWith(".")) {
            throw this.syntaxError(String.format("Strict mode error: Value '%s' ends with dot", string));
        }
        Object obj = JSONObject.stringToValue(string);
        // if obj is a boolean, look at string
        if (jsonParserConfiguration != null &&
                jsonParserConfiguration.isStrictMode()) {
            if (obj instanceof Boolean b && !"true".equals(string) && !"false".equals(string)) {
                // Strict mode only allows lowercase true or false
                throw this.syntaxError(String.format("Strict mode error: Value '%s' is not lowercase boolean", b));
            }
            else if (obj == JSONObject.NULL && !"null".equals(string)) {
                // Strint mode only allows lowercase null
                throw this.syntaxError(String.format("Strict mode error: Value '%s' is not lowercase null", obj));
            }
            else if (obj instanceof String s) {
                // Strict mode only allows strings with explicit double quotes
                throw this.syntaxError(String.format("Strict mode error: Value '%s' is not surrounded by quotes", s));
            }
        }
        return obj;
    }


    /**
     * Skip characters until the next character is the requested character.
     * If the requested character is not found, no characters are skipped.
     * @param to A character to skip to.
     * @return The requested character, or zero if the requested character
     * is not found.
     */
    public char skipTo(char to) {
        char c;
        try {
            long startIndex = this.index;
            long startCharacter = this.character;
            long startLine = this.line;
            this.reader.mark(1000000);
            do {
                c = this.next();
                if (c == 0) {
                    // in some readers, reset() may throw an exception if
                    // the remaining portion of the input is greater than
                    // the mark size (1,000,000 above).
                    this.reader.reset();
                    this.index = startIndex;
                    this.character = startCharacter;
                    this.line = startLine;
                    return 0;
                }
            } while (c != to);
            this.reader.mark(1);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
        this.back();
        return c;
    }

    /**
     * Make an IllegalArgumentException to signal a syntax error.
     *
     * @param message The error message.
     * @return  An IllegalArgumentException object, suitable for throwing
     */
    public IllegalArgumentException syntaxError(String message) {
        return new IllegalArgumentException(message + this.toString());
    }

    /**
     * Make an IllegalArgumentException to signal a syntax error.
     *
     * @param message The error message.
     * @param causedBy The throwable that caused the error.
     * @return  An IllegalArgumentException object, suitable for throwing
     */
    public IllegalArgumentException syntaxError(String message, Throwable causedBy) {
        return new IllegalArgumentException(message + this.toString(), causedBy);
    }

    /**
     * Make a printable string of this JSONTokener.
     *
     * @return " at {index} [character {character} line {line}]"
     */
    @Override
    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " +
                this.line + "]";
    }

    /**
     * Closes the underlying reader, releasing any resources associated with it.
     *
     * @throws IOException If an I/O error occurs while closing the reader.
     */
    public void close() throws IOException {
        if(reader!=null){
            reader.close();
        }
    }
}
