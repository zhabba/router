package com.test.xzha.reader.dir;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class DirReader
 * created at 05.08.15 - 10:59
 */
public class DirReader {
	private static final Logger LOG = Logger.getLogger(DirReader.class);
	ExecutorService executor;
	private ExecutorCompletionService<List<String[]>> completionService;

	public DirReader() {
		executor = Executors.newCachedThreadPool();
		completionService = new ExecutorCompletionService<>(executor);
	}


	/**
	 * Find all prefixes matched to phone
	 * @param dirPath root dir path
	 * @param phone String
	 * @return List<String[]> all records matched to given phone
	 * @throws IOException
	 */
	public List<String[]> searchPrefixDirWalk(String dirPath, String phone) throws IOException {
		FileReader.setFilesCounter(0);
		List<String[]> totalMatches = new ArrayList<>();
		Files.walkFileTree(Paths.get(dirPath), new FileReader(completionService, phone));
		int filesCounter = FileReader.getFilesCounter();
		LOG.debug("Processed " + filesCounter + " files totally ...");
		for (int i = 0; i < filesCounter; i++) {
			try {
				List<String[]> resultMap = completionService.take().get();
				if (resultMap != null) {
					totalMatches.addAll(resultMap);
				}
			} catch (InterruptedException e) {
				LOG.error("Interrupted ...", e);
			} catch (ExecutionException e) {
				LOG.error("Execution exception ...", e);
			}
		}
		executor.shutdown();
		return totalMatches;
	}
}
