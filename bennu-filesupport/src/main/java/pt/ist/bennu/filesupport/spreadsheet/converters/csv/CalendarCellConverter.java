package pt.ist.bennu.filesupport.spreadsheet.converters.csv;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pt.ist.bennu.filesupport.spreadsheet.converters.CellConverter;

public class CalendarCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
        return (source != null) ? new SimpleDateFormat("dd/MM/yyyy hh:mm").format(((Calendar) source).getTime()) : null;
    }
}
