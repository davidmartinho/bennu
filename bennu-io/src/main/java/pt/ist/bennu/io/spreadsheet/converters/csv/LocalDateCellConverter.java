package pt.ist.bennu.io.spreadsheet.converters.csv;

import org.joda.time.LocalDate;

import pt.ist.bennu.io.spreadsheet.converters.CellConverter;

public class LocalDateCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
        return (source != null) ? ((LocalDate) source).toString("dd/MM/yyyy") : null;
    }
}
