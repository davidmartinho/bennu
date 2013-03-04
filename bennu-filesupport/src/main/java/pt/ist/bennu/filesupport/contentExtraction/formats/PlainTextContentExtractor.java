package pt.ist.bennu.filesupport.contentExtraction.formats;

import java.io.IOException;
import java.io.InputStreamReader;

import pt.ist.bennu.filesupport.FileSupportException;
import pt.ist.bennu.filesupport.contentExtraction.ContentExtractor;
import pt.ist.bennu.filesupport.domain.GenericFile;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.JsonObject;

public class PlainTextContentExtractor implements ContentExtractor {
    @Override
    public JsonObject getMetadata(GenericFile file) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTextContent(GenericFile file) {
        try (InputStreamReader reader = new InputStreamReader(file.getStream(), Charsets.UTF_8)) {
            return CharStreams.toString(reader);
        } catch (IOException e) {
            throw FileSupportException.parseError(e);
        }
    }
}
