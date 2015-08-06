package com.test.xzha.reader;

import com.test.xzha.reader.dir.DirReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhabba on 06.08.15.
 */
public class DirReaderTest {
    private String dirPath = "src/test/testOps";
    private String wrongDirPath = "src/test/testOops";

    private String phone1 = "100000";
    private String phone2 = "110000";
    private String phone3 = "111000";
    private String phone4 = "111100";
    private String phone5 = "200000";
    private String phone6 = "300000";

    private DirReader dr;

    @Before
    public void init() {
        dr = new DirReader();
    }

    @After
    public void clear() {
        dr.shutdown();
    }

    @Test
    public void searchPrefixDirWalkTest() throws IOException {
        List<String[]> total1 = new ArrayList<>();
        total1.add(new String[]{"1", "0.9", "op1"});
        total1.add(new String[]{"1", "0.92", "op2"});

        List<String[]> total2 = new ArrayList<>();
        total2.add(new String[]{"1", "0.9", "op1"});
        total2.add(new String[]{"11", "5.1", "op1"});
        total2.add(new String[]{"1", "0.92", "op2"});
        total2.add(new String[]{"11", "0.5", "op2"});

        List<String[]> total3 = new ArrayList<>();
        total3.add(new String[]{"1", "0.9", "op1"});
        total3.add(new String[]{"11", "5.1", "op1"});
        total3.add(new String[]{"111", "0.17", "op1"});
        total3.add(new String[]{"1", "0.92", "op2"});
        total3.add(new String[]{"11", "0.5", "op2"});
        total3.add(new String[]{"111", "0.2", "op2"});

        List<String[]> total4 = new ArrayList<>();
        total4.add(new String[]{"1", "0.9", "op1"});
        total4.add(new String[]{"11", "5.1", "op1"});
        total4.add(new String[]{"111", "0.17", "op1"});
        total4.add(new String[]{"1", "0.92", "op2"});
        total4.add(new String[]{"11", "0.5", "op2"});
        total4.add(new String[]{"111", "0.2", "op2"});
        total4.add(new String[]{"1111", "1.0", "op2"});

        List<String[]> total5 = new ArrayList<>();
        total5.add(new String[]{"2", "1.1", "op1"});

        List<String[]> total6 = new ArrayList<>();

        assertEquals(total1.size(), dr.searchPrefixDirWalk(dirPath, phone1).size());
        assertEquals(total2.size(), dr.searchPrefixDirWalk(dirPath, phone2).size());
        assertEquals(total3.size(), dr.searchPrefixDirWalk(dirPath, phone3).size());
        assertEquals(total4.size(), dr.searchPrefixDirWalk(dirPath, phone4).size());
        assertEquals(total5.size(), dr.searchPrefixDirWalk(dirPath, phone5).size());
        assertEquals(total6.size(), dr.searchPrefixDirWalk(dirPath, phone6).size());
    }

    @Test(expected = IOException.class)
    public void searchPrefixDirWalkFailedTest() throws IOException {
        DirReader dr = new DirReader();
        dr.searchPrefixDirWalk(wrongDirPath, phone1);
    }
}
