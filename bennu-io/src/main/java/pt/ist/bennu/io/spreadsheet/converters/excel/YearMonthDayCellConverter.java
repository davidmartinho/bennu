package pt.ist.bennu.io.spreadsheet.converters.excel;

import org.joda.time.YearMonthDay;

import pt.ist.bennu.io.spreadsheet.converters.CellConverter;

public class YearMonthDayCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
        return (source != null) ? ((YearMonthDay) source).toDateTimeAtMidnight().toDate() : null;
    }
}
