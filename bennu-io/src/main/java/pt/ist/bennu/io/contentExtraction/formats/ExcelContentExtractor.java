package pt.ist.bennu.io.contentExtraction.formats;

import java.io.IOException;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.ist.bennu.io.BennuIOException;
import pt.ist.bennu.io.contentExtraction.ContentExtractor;
import pt.ist.bennu.io.domain.GenericFile;

import com.google.gson.JsonObject;

public class ExcelContentExtractor implements ContentExtractor {
    @Override
    public JsonObject getMetadata(GenericFile file) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTextContent(GenericFile file) {
        try {
            return new ExcelExtractor(new HSSFWorkbook(file.getStream())).getText();
        } catch (IOException e) {
            throw BennuIOException.parseError(e);
        }
    }
}