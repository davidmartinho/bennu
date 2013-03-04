package pt.ist.bennu.filesupport.spreadsheet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.bennu.filesupport.spreadsheet.SheetData.Cell;
import pt.ist.bennu.filesupport.spreadsheet.converters.CellConverter;
import pt.ist.bennu.filesupport.spreadsheet.converters.csv.BigDecimalCellConverter;
import pt.ist.bennu.filesupport.spreadsheet.converters.csv.CalendarCellConverter;
import pt.ist.bennu.filesupport.spreadsheet.converters.csv.DateCellConverter;
import pt.ist.bennu.filesupport.spreadsheet.converters.csv.DateTimeCellConverter;
import pt.ist.bennu.filesupport.spreadsheet.converters.csv.LocalDateCellConverter;
import pt.ist.bennu.filesupport.spreadsheet.converters.csv.MultiLanguageStringCellConverter;
import pt.ist.bennu.filesupport.spreadsheet.converters.csv.YearMonthDayCellConverter;

class CsvBuilder extends AbstractSheetBuilder {
    static Map<Class<?>, CellConverter> BASE_CONVERTERS;

    static {
        // TODO: grow this list to all common basic types.
        BASE_CONVERTERS = new HashMap<>();
        BASE_CONVERTERS.put(DateTime.class, new DateTimeCellConverter());
        BASE_CONVERTERS.put(YearMonthDay.class, new YearMonthDayCellConverter());
        BASE_CONVERTERS.put(LocalDate.class, new LocalDateCellConverter());
        BASE_CONVERTERS.put(GregorianCalendar.class, new CalendarCellConverter());
        BASE_CONVERTERS.put(Date.class, new DateCellConverter());
        BASE_CONVERTERS.put(BigDecimal.class, new BigDecimalCellConverter());
        BASE_CONVERTERS.put(MultiLanguageStringCellConverter.class, new MultiLanguageStringCellConverter());
    }

    {
        converters.putAll(BASE_CONVERTERS);
    }

    public void build(Map<String, SheetData<?>> sheets, OutputStream output, String separator) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(output)) {
            List<String> lines = new ArrayList<>();
            for (SheetData<?> data : sheets.values()) {
                if (!data.headers.get(0).isEmpty()) {
                    for (List<Cell> headerRow : data.headers) {
                        List<String> column = new ArrayList<>();
                        for (Cell cell : headerRow) {
                            column.add(cell.value.toString());
                            if (cell.span > 1) {
                                column.addAll(Arrays.asList(new String[cell.span - 1]));
                            }
                        }
                        lines.add(StringUtils.join(column, separator));
                    }
                }
                for (final List<Cell> line : data.matrix) {
                    List<String> column = new ArrayList<>();
                    for (Cell cell : line) {
                        column.add(cell.value != null ? convert(cell.value).toString() : "");
                        if (cell.span > 1) {
                            column.addAll(Arrays.asList(new String[cell.span - 1]));
                        }
                    }
                    lines.add(StringUtils.join(column, separator));
                }
            }
            writer.write(StringUtils.join(lines, "\n"));
        }
    }
}
