package com.test.xzha;

import com.test.xzha.config.RouterConfigReader;
import com.test.xzha.reader.input.InputReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

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
            Properties props = RouterConfigReader.readIniFile(args[0]);
            System.out.println(props.getProperty("prices.dir", "prices"));
        } catch (IOException e) {
            LOG.error("Can't find " + args[0], e);
            System.exit(2);
        }
        InputReader ir = new InputReader();
        String phone;
        while (true) {
            System.out.print("Please enter phone number >>> ");
            phone = ir.getInput();
            if (phone != null) {
                pr.route(phone);
            } else {
                System.out.println("Phone number can be only numeric.");
            }
        }
    }

    /**
     *
     * @param phone
     */
    private void route(String phone) {
        System.out.println(phone);
    }
}
