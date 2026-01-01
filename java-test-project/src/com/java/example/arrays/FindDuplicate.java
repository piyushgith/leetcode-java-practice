package com.java.example.arrays;

import java.util.*;


public class FindDuplicate {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 3};
        int duplicate = -1;
        Set<Integer> set = new HashSet<>();

        for (int num : arr) {
            // Check if the map already contains the number
            if (set.contains(num)) {
                duplicate = num;
                break;
            } else {
                // Put the number in the map with a count of 1
                set.add(num);
            }
        }

        if (duplicate != -1) {
            System.out.println("Duplicate element found: " + duplicate);
        } else {
            System.out.println("No duplicate elements found.");
        }
    }
}
