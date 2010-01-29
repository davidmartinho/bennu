package pt.ist.fenixframework.plugins.luceneIndexing.util;

public class LuceneStringEscaper {

    // the \\ needs to be the 1st character since it's also the escape
    // character.
    private static String[] reservedWords = { "\\", "+", "-", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~", "*", "?", ":",
	    "&&", "||" };

    private static String ESCAPE_STRING = "\\";

    public static String escape(String valueToEscape) {
	StringBuilder builder = new StringBuilder(valueToEscape);
	for (String reservedWord : reservedWords) {
	    escape(builder, reservedWord);
	}
	return builder.toString();
    }

    private static void escape(StringBuilder buffer, String reservedWord) {
	int pos = 0;
	int index = -1;
	while ((index = buffer.indexOf(reservedWord, pos)) >= 0) {
	    pos = index + 2;
	    buffer.insert(index, ESCAPE_STRING);
	}
    }
}
