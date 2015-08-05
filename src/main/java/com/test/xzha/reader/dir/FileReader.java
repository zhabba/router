package com.test.xzha.reader.dir;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


/**
 * Class com.test.xzha.reader.dir.FileVisitor
 * created at 05.08.15 - 15:58
 */
public class FileReader extends SimpleFileVisitor<Path> {
	private static final Logger LOG = Logger.getLogger(FileReader.class);
	private static final AtomicInteger filesCounter = new AtomicInteger(0);
	private ExecutorCompletionService<Map<String, String[]>> completionService;
	private volatile String phone;


	public FileReader(ExecutorCompletionService<Map<String, String[]>> completionService, String phone) {
		this.completionService = completionService;
		this.phone = phone;
	}

	/**
	 * Invoked for a file in a directory.
	 * <p>
	 * <p> Unless overridden, this method returns {@link FileVisitResult#CONTINUE
	 * CONTINUE}.
	 *
	 * @param file
	 * @param attrs
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		completionService.submit(() -> {
			LOG.debug("Read file: " + file.getFileName());
			Map<String, String[]> matches = new TreeMap<>();
			try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
				lines.forEach(line -> {
					String[] prefixPricePair = line.split("\\s");
					System.out.println("Phone: " + phone + " prefix: " + prefixPricePair[0]);
					if (phone.contains(prefixPricePair[0])) {
						matches.put(prefixPricePair[0], prefixPricePair);
					}
				});
				LOG.info("End of data source file reached ...");
			} catch (Exception e) {
				LOG.error(e);
			}
			LOG.debug("Matches: " + matches.toString());
			return matches;
		});
		LOG.debug("Currently searched in " + filesCounter.incrementAndGet() + " files ...");
		return FileVisitResult.CONTINUE;
	}


	public static int getFilesCounter() {
		return filesCounter.get();
	}

	public static void setFilesCounter (int value) {
		filesCounter.set(value);
	}
}
