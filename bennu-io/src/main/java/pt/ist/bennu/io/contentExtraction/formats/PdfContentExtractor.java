package pt.ist.bennu.io.contentExtraction.formats;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import pt.ist.bennu.io.BennuIOException;
import pt.ist.bennu.io.contentExtraction.ContentExtractor;
import pt.ist.bennu.io.domain.GenericFile;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

public class PdfContentExtractor implements ContentExtractor {
    @Override
    public JsonObject getMetadata(GenericFile file) {
        try {
            PdfReader reader = new PdfReader(file.getStream());
            JsonObject metadata = new JsonObject();
            Map<String, String> docmetadata = reader.getInfo();
            for (Entry<String, String> entry : docmetadata.entrySet()) {
                if (!Strings.isNullOrEmpty(entry.getValue())) {
                    metadata.addProperty(entry.getKey(), entry.getValue());
                }
            }
            return metadata;
        } catch (IOException e) {
            throw BennuIOException.parseError(e);
        }
    }

    @Override
    public String getTextContent(GenericFile file) {
        try {
            PdfReader reader = new PdfReader(file.getStream());
            PdfTextExtractor extractor = new PdfTextExtractor(reader);
            int pages = reader.getNumberOfPages();
            StringBuilder content = new StringBuilder();
            for (int i = 1; i <= pages; i++) {
                content.append(extractor.getTextFromPage(i));
            }
            return content.toString();
        } catch (IOException e) {
            throw BennuIOException.parseError(e);
        }
    }
}
