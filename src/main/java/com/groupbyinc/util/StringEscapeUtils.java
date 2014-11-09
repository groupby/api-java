package com.groupbyinc.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringEscapeUtils {

    private static final String COULDNT_CLOSE = "Couldn't close";
    private static final String U000 = "\\u000";
    private static final String U = "\\u";
    private static final String U00 = "\\u00";
    private static final String U0 = "\\u0";
    private static final String THE_WRITER_MUST_NOT_BE_NULL = "The Writer must not be null";
    private static final Logger LOG = Logger.getLogger(
            StringEscapeUtils.class.getName());

    private StringEscapeUtils() {
        // no public constructor
    }

    // Java and JavaScript

    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false);
    }

    public static void escapeJava(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, false);
    }

    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true);
    }

    public static void escapeJavaScript(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, true);
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes);
            return writer.toString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            LOG.log(Level.SEVERE, COULDNT_CLOSE, ioe);
            return null;
        }
    }

    private static void escapeJavaStyleString(Writer out, String str, // NOSONAR
                                              boolean escapeSingleQuote) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException(THE_WRITER_MUST_NOT_BE_NULL);
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.write(U + hex(ch));
            } else if (ch > 0xff) {
                out.write(U0 + hex(ch));
            } else if (ch > 0x7f) {
                out.write(U00 + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                case '\b':
                    out.write('\\');
                    out.write('b');
                    break;
                case '\n':
                    out.write('\\');
                    out.write('n');
                    break;
                case '\t':
                    out.write('\\');
                    out.write('t');
                    break;
                case '\f':
                    out.write('\\');
                    out.write('f');
                    break;
                case '\r':
                    out.write('\\');
                    out.write('r');
                    break;
                default:
                    if (ch > 0xf) {
                        out.write(U00 + hex(ch));
                    } else {
                        out.write(U000 + hex(ch));
                    }
                    break;
                }
            } else {
                switch (ch) {
                case '\'':
                    if (escapeSingleQuote) {
                        out.write('\\');
                    }
                    out.write('\'');
                    break;
                case '"':
                    out.write('\\');
                    out.write('"');
                    break;
                case '\\':
                    out.write('\\');
                    out.write('\\');
                    break;
                case '/':
                    out.write('\\');
                    out.write('/');
                    break;
                default:
                    out.write(ch);
                    break;
                }
            }
        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    public static String unescapeJava(String str) {
        if (str == null) {
            return null;
        }
        try {
            StringWriter writer = new StringWriter(str.length());
            unescapeJava(writer, str);
            return writer.toString();
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, COULDNT_CLOSE, ioe);
            return null;
        }
    }

    public static void unescapeJava(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException(THE_WRITER_MUST_NOT_BE_NULL);
        }
        if (str == null) {
            return;
        }
        int sz = str.length();
        StringBuilder unicode = new StringBuilder(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (inUnicode) {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);
                if (unicode.length() == 4) {
                    // unicode now contains the four hex digits
                    // which represents our unicode character
                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        out.write((char) value);
                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;
                    } catch (NumberFormatException nfe) {
                        throw new IllegalStateException(
                                "Unable to parse unicode value: " + unicode, nfe);
                    }
                }
                continue;
            }
            if (hadSlash) {
                // handle an escaped value
                hadSlash = false;
                switch (ch) {
                case '\\':
                    out.write('\\');
                    break;
                case '\'':
                    out.write('\'');
                    break;
                case '\"':
                    out.write('"');
                    break;
                case 'r':
                    out.write('\r');
                    break;
                case 'f':
                    out.write('\f');
                    break;
                case 't':
                    out.write('\t');
                    break;
                case 'n':
                    out.write('\n');
                    break;
                case 'b':
                    out.write('\b');
                    break;
                case 'u': {
                    // uh-oh, we're in unicode country....
                    inUnicode = true;
                    break;
                }
                default:
                    out.write(ch);
                    break;
                }
                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }
            out.write(ch);
        }
        if (hadSlash) {
            out.write('\\');
        }
    }

    public static String unescapeJavaScript(String str) {
        return unescapeJava(str);
    }

    public static void unescapeJavaScript(Writer out, String str) throws IOException {
        unescapeJava(out, str);
    }

}
