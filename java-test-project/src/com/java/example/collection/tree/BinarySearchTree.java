package com.java.example.collection.tree;


// Create the BinarySearchTree class
public class BinarySearchTree {
    Node root; // The root of the BST

    // Constructor for an empty BST
    public BinarySearchTree() {
        root = null;
    }

    // ==========================
    // Public API Methods
    // ==========================

    /** Public method to insert a new key */
    public void insert(int data) {
        root = insertRecursive(root, data);
    }

    /** Public method to search for a key */
    public boolean search(int data) {
        return searchRecursive(root, data);
    }

    /** Public method to delete a key */
    public void delete(int data) {
        root = deleteRecursive(root, data);
    }

    // ==========================
    // Private Recursive Helper Methods
    // ==========================

    /** Recursive helper for insertion */
    private Node insertRecursive(Node current, int data) {
        // If the tree or subtree is empty, create a new node and return it
        if (current == null) {
            return new Node(data);
        }

        // Otherwise, recur down the tree
        if (data < current.data) {
            current.left = insertRecursive(current.left, data);
        } else if (data > current.data) {
            current.right = insertRecursive(current.right, data);
        }
        // If data is equal, it's already in the tree, do nothing.
        return current;
    }

    /** Recursive helper for searching */
    private boolean searchRecursive(Node current, int data) {
        // Base Cases: root is null or data is present at root
        if (current == null) {
            return false;
        }
        if (current.data == data) {
            return true;
        }

        // Data is greater than current's data, so search in the right subtree
        if (data > current.data) {
            return searchRecursive(current.right, data);
        }

        // Data is smaller than current's data, so search in the left subtree
        return searchRecursive(current.left, data);
    }

    /** Recursive helper for deletion */
    private Node deleteRecursive(Node current, int data) {
        // Base case: If the tree is empty
        if (current == null) {
            return null;
        }

        // Recur down the tree to find the node to delete
        if (data < current.data) {
            current.left = deleteRecursive(current.left, data);
        } else if (data > current.data) {
            current.right = deleteRecursive(current.right, data);
        } else {
            // Node with the data has been found. Now, handle the 3 deletion cases.

            // CASE 1: Node is a leaf (no children)
            if (current.left == null && current.right == null) {
                return null;
            }

            // CASE 2: Node has one child
            if (current.right == null) {
                return current.left;
            }
            if (current.left == null) {
                return current.right;
            }

            // CASE 3: Node has two children
            // Find the in-order successor (smallest value in the right subtree)
            int smallestValue = findSmallestValue(current.right);
            // Replace the node's data with the successor's data
            current.data = smallestValue;
            // Delete the in-order successor from the right subtree
            current.right = deleteRecursive(current.right, smallestValue);
        }
        return current;
    }

    /** Helper function to find the smallest value in a tree */
    private int findSmallestValue(Node root) {
        return root.left == null ? root.data : findSmallestValue(root.left);
    }

    // Helper method for in-order traversal (prints sorted order)
    public void printInOrder() {
        inOrderRecursive(root);
        System.out.println();
    }

    private void inOrderRecursive(Node node) {
        if (node != null) {
            inOrderRecursive(node.left);
            System.out.print(node.data + " ");
            inOrderRecursive(node.right);
        }
    }


    // Main method to test the implementation
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();

        // Insert elements
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);
        bst.insert(60);
        bst.insert(80);

        System.out.println("In-order traversal of the initial tree:");
        bst.printInOrder(); // Expected: 20 30 40 50 60 70 80

        // Search for elements
        System.out.println("\nIs 40 in the tree? " + bst.search(40)); // Expected: true
        System.out.println("Is 90 in the tree? " + bst.search(90)); // Expected: false

        // Delete elements
        System.out.println("\n--- Deleting 20 (leaf node) ---");
        bst.delete(20);
        bst.printInOrder(); // Expected: 30 40 50 60 70 80

        System.out.println("\n--- Deleting 30 (node with one child) ---");
        bst.delete(30);
        bst.printInOrder(); // Expected: 40 50 60 70 80

        System.out.println("\n--- Deleting 50 (node with two children, the root) ---");
        bst.delete(50);
        bst.printInOrder(); // Expected: 40 60 70 80
    }
}
