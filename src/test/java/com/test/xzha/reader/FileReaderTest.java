package com.test.xzha.reader;

import com.test.xzha.reader.dir.FileReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by zhabba on 06.08.15.
 */
public class FileReaderTest {

    private ExecutorService executor;
    private ExecutorCompletionService<List<String[]>> completionService;

    private String file1 = "src/test/testOps/op1/op1.txt";
    private String wrongFilePath = "src/test/testOps/op3.txt";

    private String phone1 = "100000";

    @Before
    public void init() {
        executor = Executors.newCachedThreadPool();
        completionService = new ExecutorCompletionService<>(executor);
    }

    @After
    public void shutdown() {
        executor.shutdown();
    }

    @Test
    public void visitFileTest() throws IOException, InterruptedException, ExecutionException {
        List<String[]> total1 = new ArrayList<>();
        total1.add(new String[]{"1", "0.9", "op1"});

        FileReader fr = new FileReader(completionService, phone1);
        Path pfile1 = Paths.get(file1);
        BasicFileAttributes attr = Files.readAttributes(pfile1, PosixFileAttributes.class);
        fr.visitFile(pfile1, attr);
        List<String[]> result1 = completionService.take().get();
        assertNotNull(result1);
        assertArrayEquals(result1.toArray(), total1.toArray());
    }

    @Test(expected = IOException.class)
    public void visitFileFailedDueToWrongPathTest() throws IOException, InterruptedException, ExecutionException {
        FileReader fr = new FileReader(completionService, phone1);
        Path pfile1 = Paths.get(wrongFilePath);
        BasicFileAttributes attr = Files.readAttributes(pfile1, PosixFileAttributes.class);
        fr.visitFile(pfile1, attr);
    }

    @Test
    public void getSetFileCounterTest() {
        FileReader.setFilesCounter(5);
        assertEquals(5, FileReader.getFilesCounter());
    }
}
