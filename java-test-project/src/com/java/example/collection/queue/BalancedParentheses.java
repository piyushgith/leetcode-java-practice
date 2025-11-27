package com.java.example.collection.queue;

// File: BalancedParentheses.java

import java.util.Stack;

public class BalancedParentheses {

    // ==========================
    // Solution: Using a Stack
    // ==========================
    public static boolean isBalanced(String s) {
        // A stack to keep track of opening brackets
        Stack<Character> stack = new Stack<>();

        // Iterate through each character of the string
        for (char c : s.toCharArray()) {
            // If the character is an opening bracket, push it onto the stack
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            }
            // If the character is a closing bracket
            else if (c == ')' || c == '}' || c == ']') {
                // If the stack is empty, it means we have a closing bracket
                // with no corresponding opening bracket.
                if (stack.isEmpty()) {
                    return false;
                }

                // Pop the top element from the stack
                char top = stack.pop();

                // Check if the popped opening bracket matches the current closing bracket
                if ((c == ')' && top != '(') || (c == '}' && top != '{') || (c == ']' && top != '[')) {
                    return false; // Mismatch
                }
            }
        }

        // After processing all characters, the stack should be empty
        // if the string is balanced. Any remaining items are unmatched opening brackets.
        return stack.isEmpty();
    }

    // Main method to test the solution
    public static void main(String[] args) {
        String expr1 = "({[]})";
        String expr2 = "({[})";
        String expr3 = "((()))";
        String expr4 = "(()";
        String expr5 = ")(";
        String expr6 = ""; // Empty string is considered balanced

        System.out.println("Is '" + expr1 + "' balanced? " + isBalanced(expr1)); // Expected: true
        System.out.println("Is '" + expr2 + "' balanced? " + isBalanced(expr2)); // Expected: false (mismatch)
        System.out.println("Is '" + expr3 + "' balanced? " + isBalanced(expr3)); // Expected: true
        System.out.println("Is '" + expr4 + "' balanced? " + isBalanced(expr4)); // Expected: false (unmatched opening)
        System.out.println("Is '" + expr5 + "' balanced? " + isBalanced(expr5)); // Expected: false (unmatched closing)
        System.out.println("Is '" + expr6 + "' balanced? " + isBalanced(expr6)); // Expected: true
    }
}
