package com.java.example.collection.tree;

import java.util.Queue;
import java.util.LinkedList;

public class MaxDepthOfTree {
    Node root;

    public MaxDepthOfTree() {
        root = null;
    }

    // ==========================
    // Solution 1: Recursive Approach (Most Common)
    // ==========================

    /**
     * Calculates the maximum depth of the tree recursively.
     * Time Complexity: O(n) - we visit each node once.
     * Space Complexity: O(h) - where h is the height of the tree, due to the recursion call stack.
     */
    public int maxDepthRecursive(Node node) {
        // Base case: If the tree is empty, its depth is 0.
        if (node == null) {
            return 0;
        }

        // Recursively find the depth of the left and right subtrees
        int leftDepth = maxDepthRecursive(node.left);
        int rightDepth = maxDepthRecursive(node.right);

        // The depth of the tree is 1 (for the current node) + the max depth of its subtrees
        return 1 + Math.max(leftDepth, rightDepth);
    }

    // ==========================
    // Solution 2: Iterative Approach (BFS)
    // ==========================

    /**
     * Calculates the maximum depth of the tree using a level-order traversal.
     * Time Complexity: O(n) - we visit each node once.
     * Space Complexity: O(w) - where w is the maximum width of the tree.
     */
    public int maxDepthIterative(Node root) {
        // Base case: If the tree is empty, its depth is 0.
        if (root == null) {
            return 0;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;

        // Loop until the queue is empty
        while (!queue.isEmpty()) {
            // Increment depth for each level we process
            depth++;

            // Get the number of nodes at the current level
            int levelSize = queue.size();

            // Process all nodes at the current level
            for (int i = 0; i < levelSize; i++) {
                Node currentNode = queue.poll();

                // Add children of the current node to the queue for the next level
                if (currentNode.left != null) {
                    queue.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    queue.add(currentNode.right);
                }
            }
        }
        return depth;
    }


    // Main method to test the implementation
    public static void main(String[] args) {
        MaxDepthOfTree tree = new MaxDepthOfTree();
        /*
         * Constructing the following tree:
         *       1
         *      / \
         *     2   3
         *    / \   \
         *   4   5   6
         *  /
         * 7
         * The longest path is 1 -> 2 -> 4 -> 7, which has 4 nodes.
         * The maximum depth is 4.
         */
        tree.root = new Node(1);
        tree.root.left = new Node(2);
        tree.root.right = new Node(3);
        tree.root.left.left = new Node(4);
        tree.root.left.right = new Node(5);
        tree.root.right.right = new Node(6);
        tree.root.left.left.left = new Node(7);

        System.out.println("--- Testing Maximum Depth ---");

        int recursiveDepth = tree.maxDepthRecursive(tree.root);
        System.out.println("Maximum depth (Recursive): " + recursiveDepth); // Expected: 4

        int iterativeDepth = tree.maxDepthIterative(tree.root);
        System.out.println("Maximum depth (Iterative/BFS): " + iterativeDepth); // Expected: 4
    }
}
