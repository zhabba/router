package com.test.xzha.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        try(InputStream fileInputStream = new FileInputStream(iniPath)){
            PROPS.load(fileInputStream);
        } catch (IOException e) {
            throw new IOException(e);
        }
        return PROPS;
    }
}