package com.java.example.collection.tree;

/**
 * Find the Lowest Common Ancestor (LCA) of two nodes in a binary tree.
 * The LCA is defined as the lowest node in the tree that has both nodes as descendants
 * (where we allow a node to be a descendant of itself).
 */
public class LowestCommonAncestor {
    Node root;

    public LowestCommonAncestor() {
        root = null;
    }

    // ==========================
    // Solution: Recursive Search
    // ==========================
    public Node findLCA(Node root, Node p, Node q) {
        // Base case: If we reach a null node, we can't find an ancestor here.
        if (root == null) {
            return null;
        }

        // If the current node is one of the target nodes (p or q),
        // then this node is a potential ancestor. Return it.
        if (root == p || root == q) {
            return root;
        }

        // Recursively search for p and q in the left and right subtrees.
        Node leftLCA = findLCA(root.left, p, q);
        Node rightLCA = findLCA(root.right, p, q);

        // --- Combine Results ---
        // If both recursive calls returned a non-null node, it means
        // p was found in one subtree and q was found in the other.
        // Therefore, the current root is their lowest common ancestor.
        if (leftLCA != null && rightLCA != null) {
            return root;
        }

        // If only one of the subtrees returned a non-null node,
        // it means both p and q are located in that subtree (or only one was found).
        // The non-null result is the LCA for this subtree.
        return (leftLCA != null) ? leftLCA : rightLCA;
    }

    // Main method to test the implementation
    public static void main(String[] args) {
        LowestCommonAncestor tree = new LowestCommonAncestor();
        /*
         * Constructing the following tree:
         *       3
         *      / \
         *     5   1
         *    / \ / \
         *   6  2 0  8
         *     / \
         *    7   4
         */
        tree.root = new Node(3);
        tree.root.left = new Node(5);
        tree.root.right = new Node(1);
        tree.root.left.left = new Node(6);
        tree.root.left.right = new Node(2);
        tree.root.right.left = new Node(0);
        tree.root.right.right = new Node(8);
        tree.root.left.right.left = new Node(7);
        tree.root.left.right.right = new Node(4);

        Node p = tree.root.left;       // Node with value 5
        Node q = tree.root.right;      // Node with value 1
        Node lca1 = tree.findLCA(tree.root, p, q);
        System.out.println("LCA of " + p.data + " and " + q.data + " is: " + lca1.data); // Expected: 3

        Node p2 = tree.root.left;      // Node with value 5
        Node q2 = tree.root.left.right.right; // Node with value 4
        Node lca2 = tree.findLCA(tree.root, p2, q2);
        System.out.println("LCA of " + p2.data + " and " + q2.data + " is: " + lca2.data); // Expected: 5

        Node p3 = tree.root.left.right.left; // Node with value 7
        Node q3 = tree.root.left.right.right; // Node with value 4
        Node lca3 = tree.findLCA(tree.root, p3, q3);
        System.out.println("LCA of " + p3.data + " and " + q3.data + " is: " + lca3.data); // Expected: 2
    }
}
