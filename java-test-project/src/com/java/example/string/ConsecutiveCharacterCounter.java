package com.java.example.string;

public class ConsecutiveCharacterCounter {
    static void main(String[] args) {
        countConsecutiveCharacters("aaabbCCaaDD"); // Expected: a3b2C2a2D2
        countConsecutiveCharacters("a");           // Expected: a1
        countConsecutiveCharacters("aaa");         // Expected: a3
        countConsecutiveCharacters("abcdef");      // Expected: a1b1c1d1e1f1
        countConsecutiveCharacters("aabbcc");      // Expected: a2b2c2
        countConsecutiveCharacters("");            // Expected: (empty)
    }


    static void countConsecutiveCharacters(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Input string is empty.");
            return;
        }
        StringBuilder result = new StringBuilder();
        int count = 1;

        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == input.charAt(i - 1)) {
                // Same character as previous, increment count
                count++;
            } else {
                // Different character, append previous character and its count
                result.append(input.charAt(i - 1)).append(count);
                count = 1; // Reset count for new character
            }
        }

        // Don't forget to append the last character sequence
        result.append(input.charAt(input.length() - 1)).append(count);
        System.out.println(result.toString());
    }
}
