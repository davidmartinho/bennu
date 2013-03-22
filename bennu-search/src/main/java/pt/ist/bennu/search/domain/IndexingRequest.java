package pt.ist.bennu.search.domain;

import pt.ist.bennu.search.Indexable;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class IndexingRequest extends IndexingRequest_Base {

    public IndexingRequest(Indexable indexableObject) {
        super();
        setIndexableExternalId(((AbstractDomainObject) indexableObject).getExternalId());
        setPluginRoot(LuceneSearchPluginRoot.getInstance());
    }

    public IndexDocument getIndex() {
        AbstractDomainObject someDomainObject = AbstractDomainObject.fromExternalId(getIndexableExternalId());
        return ((Indexable) someDomainObject).getDocumentToIndex();
    }

    public void delete() {
        removePluginRoot();
        super.deleteDomainObject();
    }
}
