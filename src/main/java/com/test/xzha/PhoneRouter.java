package com.test.xzha;

import com.test.xzha.config.RouterConfigReader;
import com.test.xzha.reader.dir.DirReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public final class PhoneRouter
{
    private static final Logger LOG = Logger.getLogger(PhoneRouter.class);
    private static Properties PROPS;

    /**
     *
     * @param args
     */
    public static void main( String[] args )
    {
        if (args.length == 0) {
            System.out.println("Please provide ini file path as firs argument ...");
            System.exit(1);
        }
        PhoneRouter pr = new PhoneRouter();
        try {
            PROPS = RouterConfigReader.readIniFile(args[0]);
            Scanner input = new Scanner(System.in);
            System.out.println(PROPS.getProperty("input.prompt"));
            String phone;
            while (input.hasNext() && (!"exit".equals(phone = input.next().toLowerCase()))) {
                pr.route(phone);
                System.out.println(PROPS.getProperty("input.prompt"));
            }
            input.close();
        } catch (IOException e) {
            LOG.error("Can't find " + args[0], e);
            System.exit(2);
        }
    }

    /**
     *
     * @param phone
     */
    private void route(String phone) throws IOException {
        LOG.debug("will route phone: " + phone);
        DirReader dr = new DirReader();
        Map<String, String[]> total = dr.readDir(PROPS.getProperty("prices.dir"), phone);
        System.out.println("Total: " + total.toString());
    }
}
