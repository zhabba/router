package com.test.xzha.reader.dir;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
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
	private ExecutorCompletionService<Map<String, String[]>> completionService;

	public DirReader() {
		executor = Executors.newCachedThreadPool();
		completionService = new ExecutorCompletionService<>(executor);
	}


	public Map<String, String[]> readDir(String dirPath, String phone) throws IOException {
		FileReader.setFilesCounter(0);
		Map<String, String[]> totalMatches = new TreeMap<>();
		Files.walkFileTree(Paths.get(dirPath), new FileReader(completionService, phone));
		int filesCounter = FileReader.getFilesCounter();
		LOG.debug("Processed " + filesCounter + " files totally ...");
		for (int i = 0; i < filesCounter; i++) {
			try {
				Map<String, String[]> resultMap = completionService.take().get();
				if (resultMap != null) {
					totalMatches.putAll(resultMap);
					System.out.println("Result: " + resultMap.toString());
				}
			} catch (InterruptedException e) {
				LOG.error("Interrupted ...");
			} catch (ExecutionException e) {
				LOG.error("Execution exception ...");
			}
		}
		executor.shutdown();
		return totalMatches;
	}
}
