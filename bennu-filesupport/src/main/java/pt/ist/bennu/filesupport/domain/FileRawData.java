package pt.ist.bennu.filesupport.domain;

import pt.ist.bennu.filesupport.util.ByteArray;

/**
 * 
 * @author Shezad Anavarali Date: Aug 11, 2009
 * 
 */
public class FileRawData extends FileRawData_Base {

	public FileRawData() {
		super();
		setFileSupport(FileSupport.getInstance());
	}

	public FileRawData(String uniqueIdentification, byte[] content) {
		this();
		setContent(new ByteArray(content));
		setContentKey(uniqueIdentification);
	}

	public void delete() {
		removeFileSupport();
		deleteDomainObject();
	}
}
