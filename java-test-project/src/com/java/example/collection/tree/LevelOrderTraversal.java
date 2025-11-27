package com.java.example.collection.tree;

// File: LevelOrderTraversal.java

import java.util.Queue;
import java.util.LinkedList;


public class LevelOrderTraversal {
    Node root;

    public LevelOrderTraversal() {
        root = null;
    }

    // ==========================
    // Solution: Breadth-First Search using a Queue
    // ==========================
    public void printLevelOrder(Node root) {
        // Base case: if the tree is empty, return
        if (root == null) {
            return;
        }

        // Create a queue to hold nodes at each level
        Queue<Node> queue = new LinkedList<>();

        // Add the root node to the queue to start the process
        queue.add(root);

        // Loop as long as there are nodes to process
        while (!queue.isEmpty()) {
            // Dequeue the node at the front of the queue
            Node currentNode = queue.poll();

            // "Visit" the node (in this case, print its data)
            System.out.print(currentNode.data + " ");

            // Enqueue the left child if it exists
            if (currentNode.left != null) {
                queue.add(currentNode.left);
            }

            // Enqueue the right child if it exists
            if (currentNode.right != null) {
                queue.add(currentNode.right);
            }
        }
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        LevelOrderTraversal tree = new LevelOrderTraversal();
        /*
         * Constructing the following tree:
         *       1
         *      / \
         *     2   3
         *    / \   \
         *   4   5   6
         */
        tree.root = new Node(1);
        tree.root.left = new Node(2);
        tree.root.right = new Node(3);
        tree.root.left.left = new Node(4);
        tree.root.left.right = new Node(5);
        tree.root.right.right = new Node(6);

        System.out.println("--- Level-order Traversal (BFS) ---");
        tree.printLevelOrder(tree.root); // Expected: 1 2 3 4 5 6
        System.out.println();
    }
}
