package com.test.xzha.reader;

import com.test.xzha.reader.dir.DirReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
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

        String[][] arr1 = new String[total1.size()][3];
        List<String[]> res1 = dr.searchPrefixDirWalk(dirPath, phone1);
        String[][] arr11 = new String[res1.size()][3];

        String[][] arr2 = new String[total2.size()][3];
        List<String[]> res2 = dr.searchPrefixDirWalk(dirPath, phone2);
        String[][] arr21 = new String[res2.size()][3];

        String[][] arr3 = new String[total2.size()][3];
        List<String[]> res3 = dr.searchPrefixDirWalk(dirPath, phone3);
        String[][] arr31 = new String[res3.size()][3];

        String[][] arr4 = new String[total4.size()][3];
        List<String[]> res4 = dr.searchPrefixDirWalk(dirPath, phone4);
        String[][] arr41 = new String[res4.size()][3];

        String[][] arr5 = new String[total5.size()][3];
        List<String[]> res5 = dr.searchPrefixDirWalk(dirPath, phone5);
        String[][] arr51 = new String[res5.size()][3];

        assertArrayEquals(total1.toArray(arr1), res1.toArray(arr11));
        assertArrayEquals(total2.toArray(arr2), res2.toArray(arr21));
        assertArrayEquals(total3.toArray(arr3), res3.toArray(arr31));
        assertArrayEquals(total4.toArray(arr4), res4.toArray(arr41));
        assertArrayEquals(total5.toArray(arr5), res5.toArray(arr51));
        assertArrayEquals(total6.toArray(), dr.searchPrefixDirWalk(dirPath, phone6).toArray());
    }

    @Test(expected = IOException.class)
    public void searchPrefixDirWalkFailedTest() throws IOException {
        DirReader dr = new DirReader();
        dr.searchPrefixDirWalk(wrongDirPath, phone1);
    }
}
