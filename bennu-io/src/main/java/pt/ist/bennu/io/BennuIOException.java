package pt.ist.bennu.io;

import javax.ws.rs.core.Response.Status;

import pt.ist.bennu.core.domain.exceptions.DomainException;

public class BennuIOException extends DomainException {
    protected static final String BUNDLE = "resources.BennuIOResources";

    protected BennuIOException(String key, String... args) {
        super(BUNDLE, key, args);
    }

    protected BennuIOException(Status status, String key, String... args) {
        super(status, BUNDLE, key, args);
    }

    protected BennuIOException(Throwable cause, String key, String... args) {
        super(cause, BUNDLE, key, args);
    }

    protected BennuIOException(Throwable cause, Status status, String key, String... args) {
        super(cause, status, BUNDLE, key, args);
    }

    public static BennuIOException noContentExtractorForType(String contentType) {
        return new BennuIOException("error.bennu.io.nocontentextractorfortype", contentType);
    }

    public static BennuIOException parseError(Throwable cause) {
        return new BennuIOException(cause, "error.bennu.io.parseError");
    }

    public static BennuIOException noStorageForType(String type) {
        return new BennuIOException("error.bennu.io.notDefinedForClassType", type);
    }

    public static BennuIOException fileAccessError(Throwable cause) {
        return new BennuIOException(cause, "error.bennu.io.fileaccesserror");
    }
}
