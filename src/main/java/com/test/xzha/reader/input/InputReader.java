package com.test.xzha.reader.input;

import java.util.Scanner;

/**
 * Created by zhabba on 05.08.15.
 */
public class InputReader {
    /**
     *
     * @return
     */
    public String getInput() {
        Scanner sc = new Scanner(System.in);
        String phone = null;
        if (sc.hasNextInt()) {
            String input = sc.next().trim();
            if (input.length() != 0) {
                phone = input;
            }
        }
        return phone;
    }
}
