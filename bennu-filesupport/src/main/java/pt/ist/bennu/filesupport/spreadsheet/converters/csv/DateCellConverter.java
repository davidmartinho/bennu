package pt.ist.bennu.filesupport.spreadsheet.converters.csv;

import java.text.SimpleDateFormat;
import java.util.Date;

import pt.ist.bennu.filesupport.spreadsheet.converters.CellConverter;

public class DateCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
        return (source != null) ? new SimpleDateFormat("dd/MM/yyyy hh:mm").format((Date) source) : null;
    }
}
