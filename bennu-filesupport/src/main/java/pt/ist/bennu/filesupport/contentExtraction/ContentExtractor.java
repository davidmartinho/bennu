package pt.ist.bennu.filesupport.contentExtraction;

import pt.ist.bennu.filesupport.domain.GenericFile;

import com.google.gson.JsonObject;

public interface ContentExtractor {
    public JsonObject getMetadata(GenericFile file);

    public String getTextContent(GenericFile file);
}
