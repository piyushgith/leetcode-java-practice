package com.java.example.collection.tree;


public class ValidateBST {
    Node root;

    public ValidateBST() {
        root = null;
    }

    // ==========================
    // Solution: Recursive with Min/Max Range
    // ==========================
    public boolean isValidBST() {
        // Start the recursion with the full range of possible values
        return isValidRecursive(root, null, null);
    }

    /**
     * Helper recursive method to validate a BST.
     *
     * @param node The current node to check.
     * @param min  The minimum value the current node can have.
     * @param max  The maximum value the current node can have.
     * @return true if the subtree rooted at 'node' is a valid BST.
     */
    private boolean isValidRecursive(Node node, Integer min, Integer max) {
        // An empty tree or a null node is a valid BST.
        if (node == null) {
            return true;
        }

        // The current node's value must be within the min/max range.
        // We use null to represent negative and positive infinity.
        if ((min != null && node.data <= min) || (max != null && node.data >= max)) {
            return false;
        }

        // Recursively check the left and right subtrees with updated ranges.
        // For the left subtree, the current node's value becomes the new max.
        // For the right subtree, the current node's value becomes the new min.
        return isValidRecursive(node.left, min, node.data) && isValidRecursive(node.right, node.data, max);
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        ValidateBST validator = new ValidateBST();

        // --- Test Case 1: A valid BST ---
        System.out.println("--- Test Case 1: Valid BST ---");
        /*
         *      10
         *     /  \
         *    5    15
         *   / \   / \
         *  3   7 13  18
         */
        validator.root = new Node(10);
        validator.root.left = new Node(5);
        validator.root.right = new Node(15);
        validator.root.left.left = new Node(3);
        validator.root.left.right = new Node(7);
        validator.root.right.left = new Node(13);
        validator.root.right.right = new Node(18);

        System.out.println("Is the tree a valid BST? " + validator.isValidBST()); // Expected: true

        System.out.println("\n------------------------------------------\n");

        // --- Test Case 2: An invalid BST ---
        System.out.println("--- Test Case 2: Invalid BST ---");
        /*
         *      10
         *     /  \
         *    5    15
         *        / \
         *       6  20  <-- 6 is in the wrong place
         */
        ValidateBST invalidValidator = new ValidateBST();
        invalidValidator.root = new Node(10);
        invalidValidator.root.left = new Node(5);
        invalidValidator.root.right = new Node(15);
        invalidValidator.root.right.left = new Node(6); // Invalid: 6 is not > 10
        invalidValidator.root.right.right = new Node(20);

        System.out.println("Is the tree a valid BST? " + invalidValidator.isValidBST()); // Expected: false
    }
}