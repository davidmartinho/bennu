package pt.ist.bennu.filesupport.spreadsheet.converters.excel;

import java.util.Locale;

import pt.ist.bennu.core.util.MultiLanguageString;
import pt.ist.bennu.filesupport.spreadsheet.converters.CellConverter;

public class MultiLanguageStringCellConverter implements CellConverter {

    private Locale locale = null;

    public MultiLanguageStringCellConverter() {
    }

    public MultiLanguageStringCellConverter(final Locale language) {
        this.locale = language;
    }

    @Override
    public Object convert(Object source) {
        if (source != null) {
            final MultiLanguageString value = (MultiLanguageString) source;
            return (locale != null) ? value.getContent(locale) : value.getContent();
        }

        return null;
    }
}
