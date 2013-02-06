package pt.ist.bennu.filesupport.domain;

/**
 * 
 * @author Shezad Anavarali Date: Aug 11, 2009
 * 
 */
public class FileRawData extends FileRawData_Base {
    public FileRawData() {
        super();
    }

    public FileRawData(DomainStorage storage, String uniqueIdentification, byte[] content) {
        this();
        setStorage(storage);
        setContent(content);
        setContentKey(uniqueIdentification);
    }

    public void delete() {
        removeStorage();
        deleteDomainObject();
    }
}
