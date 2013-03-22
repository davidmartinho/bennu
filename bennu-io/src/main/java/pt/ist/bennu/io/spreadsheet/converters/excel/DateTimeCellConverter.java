package pt.ist.bennu.io.spreadsheet.converters.excel;

import org.joda.time.DateTime;

import pt.ist.bennu.io.spreadsheet.converters.CellConverter;

public class DateTimeCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
        return (source != null) ? ((DateTime) source).toDate() : null;
    }
}
