package pt.ist.bennu.filesupport;

import javax.ws.rs.core.Response.Status;

import pt.ist.bennu.core.domain.exceptions.DomainException;

public class FileSupportException extends DomainException {
    protected static final String BUNDLE = "resources.BennuFileSupportResources";

    protected FileSupportException(String key, String... args) {
        super(BUNDLE, key, args);
    }

    protected FileSupportException(Status status, String key, String... args) {
        super(status, BUNDLE, key, args);
    }

    protected FileSupportException(Throwable cause, String key, String... args) {
        super(cause, BUNDLE, key, args);
    }

    protected FileSupportException(Throwable cause, Status status, String key, String... args) {
        super(cause, status, BUNDLE, key, args);
    }

    public static FileSupportException noContentExtractorForType(String contentType) {
        return new FileSupportException("error.bennu.filesupport.nocontentextractorfortype", contentType);
    }

    public static FileSupportException parseError(Throwable cause) {
        return new FileSupportException(cause, "error.bennu.filesupport.parseError");
    }

    public static FileSupportException noStorageForType(String type) {
        return new FileSupportException("error.bennu.filesupport.notDefinedForClassType", type);
    }

    public static FileSupportException fileAccessError(Throwable cause) {
        return new FileSupportException(cause, "error.bennu.filesupport.fileaccesserror");
    }
}
