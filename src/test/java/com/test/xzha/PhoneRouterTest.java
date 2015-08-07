package com.test.xzha;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

/**
 * Unit test for simple PhoneRouter.
 */
public class PhoneRouterTest
{
    private String testConfigPath;
    private String wrongTestConfigPath;
    private String phone1 = "100000";
    private String phone2 = "110000";
    private String phone3 = "111000";
    private String phone4 = "111111";
    private String phone5 = "200000";
    private String phone6 = "300000";

    @Before
    public void init() {
        testConfigPath = "src/test/testConfig/test.ini";
        wrongTestConfigPath = "confic/tesd.ini";
    }

    @Test
    public void routeTest() throws IOException {
        String[] expectedRoute1 = new String[]{"1", "0.9", "op1"};
        String[] expectedRoute2 = new String[]{"11", "0.5", "op2"};
        String[] expectedRoute3 = new String[]{"111", "0.17", "op1"};
        String[] expectedRoute4 = new String[]{"1111", "1.0", "op2"};
        String[] expectedRoute5 = new String[]{"2", "1.1", "op1"};
        String[] expectedRoute6 = null;

        PhoneRouter pr = new PhoneRouter(testConfigPath);
        String[] route1 = pr.route(phone1);
        String[] route2 = pr.route(phone2);
        String[] route3 = pr.route(phone3);
        String[] route4 = pr.route(phone4);
        String[] route5 = pr.route(phone5);
        String[] route6 = pr.route(phone6);

        assertArrayEquals(expectedRoute1, route1);
        assertArrayEquals(expectedRoute2, route2);
        assertArrayEquals(expectedRoute3, route3);
        assertArrayEquals(expectedRoute4, route4);
        assertArrayEquals(expectedRoute5, route5);
        assertArrayEquals(expectedRoute6, route6);

    }
}
