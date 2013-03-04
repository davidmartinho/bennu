package pt.ist.bennu.filesupport.contentExtraction;

import java.util.HashMap;
import java.util.Map;

import pt.ist.bennu.filesupport.FileSupportException;
import pt.ist.bennu.filesupport.contentExtraction.formats.ExcelContentExtractor;
import pt.ist.bennu.filesupport.contentExtraction.formats.PdfContentExtractor;
import pt.ist.bennu.filesupport.contentExtraction.formats.PlainTextContentExtractor;
import pt.ist.bennu.filesupport.contentExtraction.formats.PowerPointContentExtractor;
import pt.ist.bennu.filesupport.contentExtraction.formats.WordContentExtractor;
import pt.ist.bennu.filesupport.domain.GenericFile;

import com.google.gson.JsonObject;

public class FileContentExtraction {
    private static final Map<String, ContentExtractor> extractors = new HashMap<>();

    public static void registerExtractor(ContentExtractor extractor, String... contentTypes) {
        for (String contentType : contentTypes) {
            extractors.put(contentType, extractor);
        }
    }

    public static JsonObject getMetadata(GenericFile file) {
        if (extractors.containsKey(file.getContentType())) {
            return extractors.get(file.getContentType()).getMetadata(file);
        }
        throw FileSupportException.noContentExtractorForType(file.getContentType());
    }

    public static String getTextContent(GenericFile file) {
        if (extractors.containsKey(file.getContentType())) {
            return extractors.get(file.getContentType()).getTextContent(file);
        }
        throw FileSupportException.noContentExtractorForType(file.getContentType());
    }

    static {
        // register default content extractors
        registerExtractor(new ExcelContentExtractor(), "application/vnd.ms-excel");
        registerExtractor(new PdfContentExtractor(), "application/pdf");
        registerExtractor(new PlainTextContentExtractor(), "application/rtf", "application/xml", "text/html", "text/css",
                "text/plain", "text/richtext");
        registerExtractor(new PowerPointContentExtractor(), "application/vnd.ms-powerpoint");
        registerExtractor(new WordContentExtractor(), "application/msword");
    }
}
