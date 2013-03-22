package pt.ist.bennu.io.spreadsheet.converters.excel;

import pt.ist.bennu.io.spreadsheet.converters.CellConverter;

public class IntegerCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
        final Integer value = (Integer) source;
        return (value != null) ? new Double(value.doubleValue()) : null;
    }
}
