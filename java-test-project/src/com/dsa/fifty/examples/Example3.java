package com.dsa.fifty.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Merge Intervals
 * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 * Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
 */
public class Example3 {
    static void main(String[] args) {
        Example3 example = new Example3();
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] mergedIntervals = example.merge(intervals);
        System.out.println(STR."Merged Intervals: \{Arrays.deepToString(mergedIntervals)}");
    }

    public int[][] merge(int[][] intervals) {
        // Sort intervals based on start time
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> merged = new ArrayList<>();
        int[] prev = intervals[0];

        for (int i = 1; i < intervals.length; i++) {
            // Overlap condition: current start <= previous end
            if (intervals[i][0] <= prev[1]) {
                // Merge intervals
                prev[1] = Math.max(prev[1], intervals[i][1]);
            } else {
                // No overlap, add previous interval to result
                merged.add(prev);
                prev = intervals[i];
            }
        }
        merged.add(prev); // Add the last interval
        return merged.toArray(new int[merged.size()][]);
    }
}
