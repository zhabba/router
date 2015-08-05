package com.test.xzha;

import com.test.xzha.config.RouterConfigReader;
import com.test.xzha.reader.dir.DirReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

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
            System.out.println("Please provide ini file path as first argument ...");
            System.exit(1);
        }
        PhoneRouter pr = new PhoneRouter();
        try {
            PROPS = RouterConfigReader.readIniFile(args[0]);
            Scanner input = new Scanner(System.in);
            System.out.print(PROPS.getProperty("input.prompt"));
            String phone;
            while (input.hasNext() && (!"exit".equals(phone = input.next().toLowerCase()))) {
                //TODO:: check phone is numeric
                pr.route(phone);
                System.out.print(PROPS.getProperty("input.prompt"));
            }
            input.close();
        } catch (IOException e) {
            LOG.error("Got error: ", e);
            System.exit(2);
        }
    }

    /**
     *
     * @param phone
     */
    private void route(String phone) throws IOException {
        LOG.debug("will route phone: " + phone);
        DirReader dirReader = new DirReader();
        List<String[]> total = dirReader.readDir(PROPS.getProperty("prices.dir"), phone);
        if (!total.isEmpty()) {
            Comparator<String[]> comparatorByPrefixLength = new CompareByPrefixLength();
            Comparator<String[]> comparatorByPrefThenPrice = comparatorByPrefixLength.thenComparing(new CompareByPrice());
            String[] rout = Collections.max(total, comparatorByPrefThenPrice);
            System.out.println(String.format("Route found: prefix - %s, price - %s, operator - %s.", rout[0], rout[1], rout[2]));
        } else {
            System.out.println("No route available for phone " + phone);
        }
    }

    private class CompareByPrefixLength implements Comparator<String[]> {
        @Override
        public int compare(String[] o1, String[] o2) {
            String prefix1 = o1[0];
            String prefix2 = o2[0];
            if (prefix1.length() > prefix2.length()) {
                return 1;
            } else if (prefix1.length() < prefix2.length()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private class CompareByPrice implements Comparator<String[]> {
        @Override
        public int compare(String[] o1, String[] o2) {
            double price1 = Double.parseDouble(o1[1]);
            double price2 = Double.parseDouble(o2[1]);
            if (price1 < price2) {
                return 1;
            } else if (price1 > price2) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
