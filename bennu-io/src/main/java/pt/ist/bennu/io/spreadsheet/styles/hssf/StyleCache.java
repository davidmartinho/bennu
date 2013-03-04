package pt.ist.bennu.io.spreadsheet.styles.hssf;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class StyleCache {
    private HSSFWorkbook book;

    private Map<CellStyle, HSSFCellStyle> cache = new HashMap<>();

    public StyleCache(HSSFWorkbook book) {
        this.book = book;
    }

    public HSSFCellStyle getStyle(CellStyle style) {
        if (!cache.containsKey(style)) {
            cache.put(style, style.getStyle(book));
        }
        return cache.get(style);
    }

    public int getSize() {
        return cache.size();
    }
}
