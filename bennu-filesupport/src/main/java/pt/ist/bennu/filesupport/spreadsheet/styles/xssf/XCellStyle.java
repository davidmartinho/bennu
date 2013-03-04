package pt.ist.bennu.filesupport.spreadsheet.styles.xssf;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pt.ist.bennu.filesupport.spreadsheet.styles.ICellStyle;

public abstract class XCellStyle implements ICellStyle {
    public XSSFCellStyle getStyle(XSSFWorkbook book) {
        XSSFCellStyle style = book.createCellStyle();
        appendToStyle(book, style, null);
        return style;
    }

    protected abstract void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font);
}
