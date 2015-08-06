package com.test.xzha.reader.dir;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


/**
 * Class com.test.xzha.reader.dir.FileReader
 * created at 05.08.15 - 15:58
 */
public class FileReader extends SimpleFileVisitor<Path> {
	private static final Logger LOG = Logger.getLogger(FileReader.class);
	private static final AtomicInteger filesCounter = new AtomicInteger(0);
	private ExecutorCompletionService<List<String[]>> completionService;
	private volatile String phone;


	public FileReader(ExecutorCompletionService<List<String[]>> completionService, String phone) {
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
            List<String[]> matches = new ArrayList<>();
			try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
				lines.forEach(line -> {
                    if (!line.isEmpty()) {
                        String[] prefixPriceOperator = Arrays.copyOf(line.split("\\s+"), 3);
						// Here is a place for optimisation - use not regex based search but char index based search
						if (phone.startsWith(prefixPriceOperator[0])) {
                            String operator = file.getParent().getFileName().toString();
                            prefixPriceOperator[prefixPriceOperator.length - 1] = operator;
                            matches.add(prefixPriceOperator);
                        }
                    }
                });
			} catch (Exception e) {
				LOG.error(e);
			}
			LOG.debug("File:  " + file.getFileName() + " Matches found:"+  + matches.size() );
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
