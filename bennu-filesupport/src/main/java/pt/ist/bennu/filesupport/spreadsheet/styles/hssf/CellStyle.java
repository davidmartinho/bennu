package pt.ist.bennu.filesupport.spreadsheet.styles.hssf;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.ist.bennu.filesupport.spreadsheet.styles.ICellStyle;

public abstract class CellStyle implements ICellStyle {
    public HSSFCellStyle getStyle(HSSFWorkbook book) {
        HSSFCellStyle style = book.createCellStyle();
        appendToStyle(book, style, null);
        return style;
    }

    protected abstract void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font);
}
