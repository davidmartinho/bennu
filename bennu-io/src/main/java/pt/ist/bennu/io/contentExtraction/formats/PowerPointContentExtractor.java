package pt.ist.bennu.io.contentExtraction.formats;

import java.io.IOException;

import org.apache.poi.hslf.extractor.PowerPointExtractor;

import pt.ist.bennu.io.BennuIOException;
import pt.ist.bennu.io.contentExtraction.ContentExtractor;
import pt.ist.bennu.io.domain.GenericFile;

import com.google.gson.JsonObject;

public class PowerPointContentExtractor implements ContentExtractor {
    @Override
    public JsonObject getMetadata(GenericFile file) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTextContent(GenericFile file) {
        try {
            return new PowerPointExtractor(file.getStream()).getText();
        } catch (IOException e) {
            throw BennuIOException.parseError(e);
        }
    }
}
