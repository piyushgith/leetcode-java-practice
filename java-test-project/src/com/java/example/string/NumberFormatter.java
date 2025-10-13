package com.java.example.string;

public class NumberFormatter {
    static void main(String[] args) {
        System.out.println(formatNumber("457241")); // Expected: 45-72*41
        System.out.println(formatNumber("2468"));   // Expected: 2*4*6*8
        System.out.println(formatNumber("1357"));   // Expected: 1-3-5-7
        System.out.println(formatNumber("1023"));  // Expected: 1023 (0 is ignored)
        System.out.println(formatNumber("2040"));  // Expected: 2040 (0s are ignored)
        System.out.println(formatNumber("12045")); // Expected: 12045 (0 breaks sequence)
    }

    public static String formatNumber(String number) {
        if (number == null || number.length() <= 1) {
            return number;
        }
        StringBuilder result = new StringBuilder();
        result.append(number.charAt(0));

        for (int i = 1; i < number.length(); i++) {
            char current = number.charAt(i);
            char previous = number.charAt(i - 1);

            // Skip if either digit is '0'
            if (current == '0' || previous == '0') {
                result.append(current);
                continue;
            }
            // Check if both are even
            if (isEven(previous) && isEven(current)) {
                result.append('*');
            }
            // Check if both are odd
            else if (isOdd(previous) && isOdd(current)) {
                result.append('-');
            }
            result.append(current);
        }
        return result.toString();
    }

    private static String formatNumber1(String number) {

        StringBuilder formatted = new StringBuilder();
        int n = number.length();

        for (int i = 0; i < n; i++) {
            char current = number.charAt(i);
            formatted.append(current);

            if (i < n - 1) {
                char next = number.charAt(i + 1);

                if (current != '0' && next != '0') {
                    if (isEven(current) && isEven(next)) {
                        formatted.append('*');
                    } else if (isOdd(current) && isOdd(next)) {
                        formatted.append('-');
                    }
                }
            }
        }

        //System.out.println(formatted.toString());
        return formatted.toString();
    }

    private static boolean isEven(char c) {
        int digit = c - '0';
        return digit % 2 == 0;
    }

    private static boolean isOdd(char c) {
        int digit = c - '0';
        return digit % 2 == 1;
    }


}
