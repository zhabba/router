package com.test.xzha.reader;

import com.test.xzha.reader.dir.DirReader;
import net.sf.ehcache.CacheManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by zhabba on 06.08.15.
 */
public class DirReaderTest {
    private String dirPath = "src/test/testOps";
    private String wrongDirPath = "src/test/testOops";

    private String[] phones = new String[]{"100000", "110000", "111000", "111100", "200000", "300000"};

    private DirReader dr;

    @Before
    public void init() {
        CacheManager.getInstance().clearAll();
        dr = new DirReader();
    }

    @After
    public void clear() {
        dr.shutdown();
    }

    @Test
    public void searchPrefixDirWalkTest() throws IOException {
        String[][] total1 = new String[][]{
                new String[]{"1", "0.9", "op1"},
                new String[]{"1", "0.92", "op2"}
        };

        String[][] total2 = new String[][]{
                new String[]{"1", "0.9", "op1"},
                new String[]{"11", "5.1", "op1"},
                new String[]{"1", "0.92", "op2"},
                new String[]{"11", "0.5", "op2"}
        };

        String[][] total3 = new String[][]{
                new String[]{"1", "0.9", "op1"},
                new String[]{"11", "5.1", "op1"},
                new String[]{"111", "0.17", "op1"},
                new String[]{"1", "0.92", "op2"},
                new String[]{"11", "0.5", "op2"},
                new String[]{"111", "0.2", "op2"}
        };

        String[][] total4 = new String[][]{
                new String[]{"1", "0.9", "op1"},
                new String[]{"11", "5.1", "op1"},
                new String[]{"111", "0.17", "op1"},
                new String[]{"1", "0.92", "op2"},
                new String[]{"11", "0.5", "op2"},
                new String[]{"111", "0.2", "op2"},
                new String[]{"1111", "1.0", "op2"}
        };

        String[][] total5 = new String[][]{
                new String[]{"2", "1.1", "op1"}
        };

        String[][] total6 = new String[][]{};

        String[][][] expects = new String[][][]{total1, total2, total3, total4, total5, total6};

        System.out.println(phones.length);
        for (int i = 0; i < phones.length; i++) {
            List<String[]> routesAvailable = dr.searchPrefixDirWalk(dirPath, phones[i]);
            String[][] arr = new String[routesAvailable.size()][3];
            for (int j = 0; j < routesAvailable.size(); j++) {
                arr[j] = routesAvailable.get(j);
            }
            Comparator<String[]> comparatorByPrefix = new CompareMd(0);
            Comparator<String[]> comparatorByPrexixAndOp = comparatorByPrefix.thenComparing(new CompareMd(2));
            Arrays.sort(expects[i], comparatorByPrexixAndOp);
            Arrays.sort(arr, comparatorByPrexixAndOp);
            assertArrayEquals(expects[i], arr);
        }
    }

    @Test(expected = IOException.class)
    public void searchPrefixDirWalkFailedTest() throws IOException {
        DirReader dr = new DirReader();
        dr.searchPrefixDirWalk(wrongDirPath, phones[0]);
    }

    private static class CompareMd implements Comparator<String[]> {// descending order
        private int indexToCompareOn;

        public CompareMd(int indexToCompareOn) {
            this.indexToCompareOn = indexToCompareOn;
        }

        @Override
        public int compare(String[] o1, String[] o2) {
            return o1[indexToCompareOn].compareTo(o2[indexToCompareOn]);
        }
    }
}
