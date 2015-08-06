package com.test.xzha.config;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by zhabba on 06.08.15.
 */
public class ConfigReaderTest {
    private String testConfigPath;
    private String wrongTestConfigPath;

    @Before
    public void init(){
        testConfigPath = "src/test/testConfig/test.ini";
        wrongTestConfigPath = "confic/tesd.ini";
    }

    @Test
    public void readIniFileTest() throws IOException {
        Properties properties = RouterConfigReader.readIniFile(testConfigPath);
        assertNotNull(properties);
        assertEquals(properties.get("test.value.one"), "1");
        assertEquals(properties.get("test.value.two"), "2");
    }

    @Test(expected = IOException.class)
    public void readIniFileFailedTest() throws IOException {
        Properties properties = RouterConfigReader.readIniFile(wrongTestConfigPath);
    }
}
