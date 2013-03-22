package pt.ist.bennu.io.spreadsheet.converters.excel;

import org.joda.time.LocalDate;

import pt.ist.bennu.io.spreadsheet.converters.CellConverter;

public class LocalDateCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
        return (source != null) ? ((LocalDate) source).toDateTimeAtStartOfDay().toDate() : null;
    }
}
