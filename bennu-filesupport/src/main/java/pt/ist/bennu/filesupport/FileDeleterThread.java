package pt.ist.bennu.filesupport;

import java.util.ArrayList;

import jvstm.TransactionalCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.filesupport.domain.FileSupport;
import pt.ist.bennu.filesupport.domain.LocalFileToDelete;
import pt.ist.fenixframework.pstm.Transaction;

public class FileDeleterThread implements Runnable {

	private static final long SLEEP_TIME = 300000;
	private static final Logger logger = LoggerFactory.getLogger(FileDeleterThread.class);

	@Override
	public void run() {
		try {
			Thread.sleep(SLEEP_TIME);
			logger.debug("Tick!");
			process();
		} catch (InterruptedException e) {
			// The application is shutting down...
			return;
		}
	}

	private void process() {
		Transaction.withTransaction(new TransactionalCommand() {
			@Override
			public void doIt() {
				for (final LocalFileToDelete localFileToDelete : new ArrayList<>(FileSupport.getInstance()
						.getLocalFilesToDelete())) {
					logger.info("Deleting: " + localFileToDelete.getFilePath());
					try {
						localFileToDelete.delete();
					} catch (Exception e) {
						logger.debug("Failed to delete file", e);
					}
				}
			}
		});
	}
}
