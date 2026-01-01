package com.dsa.fifty.examples;

/**
 * Maximum Subarray Sum - Kadane's Algorithm
 * Input: arr[] = [2, 3, -8, 7, -1, 2, 3]
 * Output: 11
 * Explanation: The subarray [7, -1, 2, 3] has the largest sum 11.
 * <p>
 * Input: arr[] = [-2, -4]
 * Output: -2
 * Explanation: The subarray [-2] has the largest sum -2.
 * <p>
 * Input: arr[] = [5, 4, 1, 7, 8]
 * Output: 25
 * Explanation: The subarray [5, 4, 1, 7, 8] has the largest sum 25.
 **/
public class Example1 {
    static void main(String[] args) {
        System.out.println("Hello, World!");
        int[] arr = {2, 3, -8, 7, -1, 2, 3};
        int result = maxSubArraySum(arr);
        System.out.println("Maximum Subarray Sum: " + result);
    }

    public static int maxSubArraySum(int[] arr) {
        int maxSoFar = arr[0];
        int maxEndingHere = arr[0];

        for (int i = 1; i < arr.length; i++) {
            // Update maxEndingHere to include the current element or start a new subarray
            maxEndingHere = Math.max(arr[i], maxEndingHere + arr[i]);
            // Update maxSoFar if we found a new maximum
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        return maxSoFar;
    }

}
