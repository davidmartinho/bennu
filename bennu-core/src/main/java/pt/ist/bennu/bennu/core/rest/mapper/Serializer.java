package pt.ist.bennu.bennu.core.rest.mapper;

public interface Serializer {

	public String serialize(Object object);

	public String serialize(Object object, String collectionKey);

	public void setDelegate(Serializer delegate);

}
