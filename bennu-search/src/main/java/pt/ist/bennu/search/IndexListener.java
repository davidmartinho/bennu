package pt.ist.bennu.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import pt.ist.bennu.search.domain.IndexDocument;
import pt.ist.bennu.search.domain.IndexingRequest;
import pt.ist.bennu.search.servlets.LuceneSearchPlugin;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.pstm.CommitListener;
import pt.ist.fenixframework.pstm.TopLevelTransaction;

public class IndexListener implements CommitListener {
    @Override
    public void afterCommit(TopLevelTransaction topLevelTransaction) {

    }

    @Override
    public void beforeCommit(TopLevelTransaction topLevelTransaction) {
        if (topLevelTransaction.isWriteTransaction()) {
            long t1 = System.currentTimeMillis();
            Map<Indexable, IndexDocument> newIndexes = new HashMap<Indexable, IndexDocument>();

            for (DomainObject domainObject : new HashSet<DomainObject>(topLevelTransaction.getNewObjects())) {
                if (domainObject instanceof Searchable) {
                    for (Indexable indexableObject : ((Searchable) domainObject).getObjectsToIndex()) {
                        switch (indexableObject.getIndexMode()) {
                        case ASYNC:
                            new IndexingRequest(indexableObject);
                            break;
                        case SYNC:
                            newIndexes.put(indexableObject, indexableObject.getDocumentToIndex());
                            break;
                        case MANUAL:
                            break;
                        }
                    }
                }
            }

            for (DomainObject domainObject : topLevelTransaction.getModifiedObjects()) {
                if (!topLevelTransaction.isDeleted(domainObject)) {
                    if (domainObject instanceof Searchable) {
                        for (Indexable indexableObject : ((Searchable) domainObject).getObjectsToIndex()) {
                            switch (indexableObject.getIndexMode()) {
                            case ASYNC:
                                new IndexingRequest(indexableObject);
                                break;
                            case SYNC:
                                newIndexes.put(indexableObject, indexableObject.getDocumentToIndex());
                                break;
                            case MANUAL:
                                break;
                            }
                        }
                    }
                }
            }

            if (!newIndexes.isEmpty()) {
                long t2 = System.currentTimeMillis();
                if (newIndexes.size() > 500) {
                    for (Indexable indexable : newIndexes.keySet()) {
                        new IndexingRequest(indexable);
                    }
                } else {
                    DomainIndexer.getInstance().indexDomainObjects(newIndexes.values());
                }
                long t3 = System.currentTimeMillis();
                LuceneSearchPlugin.LOGGER.info("indexed " + newIndexes.size() + " objects in: " + (t3 - t1) + " (" + (t2 - t1)
                        + ", " + (t3 - t2) + ") ms.");
            }
        }
    }
}
