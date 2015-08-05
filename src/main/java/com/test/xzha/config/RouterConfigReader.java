package com.test.xzha.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhabba on 05.08.15.
 */
public final class RouterConfigReader {
    private static final Properties PROPS = new Properties();

    /**
     *
     * @param iniPath
     * @return
     * @throws IOException
     */
    public static Properties readIniFile(String iniPath) throws IOException {
        PROPS.load(new FileInputStream(iniPath));
        return PROPS;
    }
}