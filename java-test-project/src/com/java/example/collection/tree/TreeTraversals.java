package com.java.example.collection.tree;

// File: TreeTraversals.java

import java.util.Stack;


public class TreeTraversals {
    Node root;

    public TreeTraversals() {
        root = null;
    }

    // ==========================
    // 1. IN-ORDER TRAVERSAL
    // ==========================
    // --- Recursive ---
    public void printInOrderRecursive(Node node) {
        if (node == null) return;
        printInOrderRecursive(node.left);
        System.out.print(node.data + " ");
        printInOrderRecursive(node.right);
    }

    // --- Iterative ---
    public void printInOrderIterative(Node node) {
        Stack<Node> stack = new Stack<>();
        Node current = node;

        while (current != null || !stack.isEmpty()) {
            // Reach the leftmost node of the current subtree
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // Current must be null at this point. Pop the top node.
            current = stack.pop();
            System.out.print(current.data + " ");

            // Now, visit the right subtree
            current = current.right;
        }
    }

    // ==========================
    // 2. PRE-ORDER TRAVERSAL
    // ==========================
    // --- Recursive ---
    public void printPreOrderRecursive(Node node) {
        if (node == null) return;
        System.out.print(node.data + " ");
        printPreOrderRecursive(node.left);
        printPreOrderRecursive(node.right);
    }

    // --- Iterative ---
    public void printPreOrderIterative(Node node) {
        if (node == null) return;
        Stack<Node> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            System.out.print(current.data + " ");

            // Push right child first so that left child is processed first
            if (current.right != null) {
                stack.push(current.right);
            }
            if (current.left != null) {
                stack.push(current.left);
            }
        }
    }

    // ==========================
    // 3. POST-ORDER TRAVERSAL
    // ==========================
    // --- Recursive ---
    public void printPostOrderRecursive(Node node) {
        if (node == null) return;
        printPostOrderRecursive(node.left);
        printPostOrderRecursive(node.right);
        System.out.print(node.data + " ");
    }

    // --- Iterative (using two stacks) ---
    public void printPostOrderIterative(Node node) {
        if (node == null) return;
        Stack<Node> stack1 = new Stack<>();
        Stack<Node> stack2 = new Stack<>();

        stack1.push(node);

        // Run while first stack is not empty
        while (!stack1.isEmpty()) {
            // Pop an item from stack1 and push it to stack2
            Node temp = stack1.pop();
            stack2.push(temp);

            // Push left and right children of the popped item to stack1
            if (temp.left != null) {
                stack1.push(temp.left);
            }
            if (temp.right != null) {
                stack1.push(temp.right);
            }
        }

        // Print all elements of second stack
        while (!stack2.isEmpty()) {
            Node temp = stack2.pop();
            System.out.print(temp.data + " ");
        }
    }

    // Main method to test all traversals
    public static void main(String[] args) {
        TreeTraversals tree = new TreeTraversals();
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

        System.out.println("--- In-order Traversal (Left, Root, Right) ---");
        System.out.print("Recursive:   ");
        tree.printInOrderRecursive(tree.root); // Expected: 4 2 5 1 3 6
        System.out.print("\nIterative:   ");
        tree.printInOrderIterative(tree.root); // Expected: 4 2 5 1 3 6

        System.out.println("\n\n--- Pre-order Traversal (Root, Left, Right) ---");
        System.out.print("Recursive:   ");
        tree.printPreOrderRecursive(tree.root); // Expected: 1 2 4 5 3 6
        System.out.print("\nIterative:   ");
        tree.printPreOrderIterative(tree.root); // Expected: 1 2 4 5 3 6

        System.out.println("\n\n--- Post-order Traversal (Left, Right, Root) ---");
        System.out.print("Recursive:   ");
        tree.printPostOrderRecursive(tree.root); // Expected: 4 5 2 6 3 1
        System.out.print("\nIterative:   ");
        tree.printPostOrderIterative(tree.root); // Expected: 4 5 2 6 3 1
    }
}
