package pt.ist.bennu.io.contentExtraction;

import pt.ist.bennu.io.domain.GenericFile;

import com.google.gson.JsonObject;

public interface ContentExtractor {
    public JsonObject getMetadata(GenericFile file);

    public String getTextContent(GenericFile file);
}
