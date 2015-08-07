package com.test.xzha;

import com.test.xzha.cache.RouterCache;
import com.test.xzha.config.RouterConfigReader;
import com.test.xzha.reader.dir.DirReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

public final class PhoneRouter {
    private static final Logger LOG = Logger.getLogger(PhoneRouter.class);
    private Properties props;
    private RouterCache<String[]> cache;
    private DirReader dirReader;

    public PhoneRouter(String iniPath) throws IOException {
        // read config
        props = RouterConfigReader.readIniFile(iniPath);

        // init cache
        String cacheName = props.getProperty("cache.route.name", "routes");
        cache = new RouterCache<>(cacheName);

        // init DirReader
        dirReader = new DirReader();
    }

    /**
     * Entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        // check argument is provided
        if (args.length == 0) {
            System.out.println("Please provide ini file path as first argument ...");
            System.exit(1);
        }
        try {
            PhoneRouter pr = new PhoneRouter(args[0]);
            pr.getInputAndProcess();
        } catch (IOException e) {
            System.exit(2);
        }

    }

    /**
     * Read STDIN and process phone
     *
     * @throws IOException
     */
    private void getInputAndProcess() throws IOException {
        // read phone from STDIN
        Scanner input = new Scanner(System.in);
        System.out.print(props.getProperty("input.prompt"));
        String phone;
        while (input.hasNext() && (!"exit".equals(phone = input.next().toLowerCase()))) {
            if (isNumeric(phone)) {
                displayRoute(phone, route(phone));
            } else {
                System.out.println("Phone must contain only digits.");
            }
            System.out.print(props.getProperty("input.prompt"));
        }

        // free resources
        input.close();
        cache.clear();
        cache.getCacheManager().shutdown();
        dirReader.shutdown();
    }

    /**
     * Find optimal route for given phone number
     *
     * @param phone String
     */
    public String[] route(String phone) throws IOException {
        LOG.debug("will route phone: " + phone);
        String[] route = cache.get(phone);
        if (route == null) { // not find route in cache, will search in files
            route = scratchDisk(phone);
        }
        return route;
    }

    /**
     * Just display route if any and related messages
     *
     * @param route
     */
    private void displayRoute(String phone, String[] route) {
        String message;
        if (route == null || route.length == 0) { // no route for given phone
            message = "No route available for phone " + phone;
        } else { // ok, we have route
            message = String.format("Route found: prefix - %s, price - %s, operator - %s.", route[0], route[1], route[2]);
        }
        System.out.println(message);
        LOG.info(message);
    }

    /**
     * Traverse all available files with prices to find prefix and price
     *
     * @param phone
     * @return
     * @throws IOException
     */
    private String[] scratchDisk(String phone) throws IOException {
        String[] route = null;
        List<String[]> total = dirReader.searchPrefixDirWalk(props.getProperty("prices.dir"), phone);
        if (!total.isEmpty()) {
            Comparator<String[]> comparatorByPrefixLength = new CompareByPrefixLength();
            Comparator<String[]> comparatorByPrefThenPrice = comparatorByPrefixLength.thenComparing(new CompareByPrice());
            route = Collections.max(total, comparatorByPrefThenPrice);
            cache.put(phone, route); // store route in cache
        } else {
            cache.put(phone, new String[0]); // store poison pill to indicate that no route for phone even in files
        }
        return route;
    }


    /**
     * Check whether a string consists of digits
     * (much faster than regexp)
     *
     * @param str String
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        int size = str.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return size > 0;
    }

    // comparators
    private static class CompareByPrefixLength implements Comparator<String[]> {// descending order

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

    private static class CompareByPrice implements Comparator<String[]> {// ascending order

        @Override
        public int compare(String[] o1, String[] o2) {
            double price1 = Double.parseDouble(o1[1]);
            double price2 = Double.parseDouble(o2[1]);
            return Double.compare(price2, price1);
        }
    }
}
