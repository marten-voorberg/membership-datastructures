package datastructures;

// This is a slighly altered version fo the Splay tree by Josh Israel.
/******************************************************************************
 *  Compilation:  javac SplayBST.java
 *  Execution:    java SplayBST
 *  Dependencies: none
 *
 *  Splay tree. Supports splay-insert, -search, and -delete.
 *  Splays on every operation, regardless of the presence of the associated
 *  key prior to that operation.
 *
 *  Written by Josh Israel.
 ******************************************************************************/


public class SplayTree implements Membership {
    private Node root;   // root of the BST

    // BST helper node read type
    private class Node {
        private int key;            // key
        private Node left, right;   // left and right subtrees

        public Node(int key) {
            this.key = key;
        }
    }


    // return value associated with the given key
    // if no such value, return null
    public boolean isMember(int key) {

        root = splay(root, key);
        return root.key == key;
    }

    /***************************************************************************
     *  Splay tree insertion.
     ***************************************************************************/
    public void insert(int key) {
        // splay key to root
        if (root == null) {
            root = new Node(key);
            return;
        }

        root = splay(root, key);

        // Insert new node at root
        if (key < root.key) {
            Node n = new Node(key);
            n.left = root.left;
            n.right = root;
            root.left = null;
            root = n;
        }

        // Insert new node at root
        else if (key > root.key) {
            Node n = new Node(key);
            n.right = root.right;
            n.left = root;
            root.right = null;
            root = n;
        }
    }

    /***************************************************************************
     *  Splay tree deletion.
     ***************************************************************************/
    /* This splays the key, then does a slightly modified Hibbard deletion on
     * the root (if it is the node to be deleted; if it is not, the key was
     * not in the tree). The modification is that rather than swapping the
     * root (call it node A) with its successor, it's successor (call it Node B)
     * is moved to the root position by splaying for the deletion key in A's
     * right subtree. Finally, A's right child is made the new root's right
     * child.
     */
    public void delete(int key) {
        if (root == null) return; // empty tree

        root = splay(root, key);

        if (key == root.key) {
            if (root.left == null) {
                root = root.right;
            }
            else {
                Node x = root.right;
                root = root.left;
                splay(root, key);
                root.right = x;
            }
        }

        // else: it wasn't in the tree to remove
    }


    /***************************************************************************
     * Splay tree function.
     * **********************************************************************/
    // splay key in the tree rooted at Node h. If a node with that key exists,
    //   it is splayed to the root of the tree. If it does not, the last node
    //   along the search path for the key is splayed to the root.
    private Node splay(Node h, int key) {
        if (h == null) return null;

        if (key < h.key) {
            // key not in tree, so we're done
            if (h.left == null) {
                return h;
            }
            if (key < h.left.key) {
                h.left.left = splay(h.left.left, key);
                h = rotateRight(h);
            }
            else if (key > h.left.key) {
                h.left.right = splay(h.left.right, key);
                if (h.left.right != null)
                    h.left = rotateLeft(h.left);
            }

            if (h.left == null) return h;
            else                return rotateRight(h);
        }

        else if (key > h.key) {
            // key not in tree, so we're done
            if (h.right == null) {
                return h;
            }

            if (key < h.right.key) {
                h.right.left  = splay(h.right.left, key);
                if (h.right.left != null)
                    h.right = rotateRight(h.right);
            }
            else if (key > h.right.key) {
                h.right.right = splay(h.right.right, key);
                h = rotateLeft(h);
            }

            if (h.right == null) return h;
            else                 return rotateLeft(h);
        }

        else return h;
    }


    /***************************************************************************
     *  Helper functions.
     ***************************************************************************/

    // height of tree (1-node tree has height 0)
    public int height() { return height(root); }
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }


    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return 1 + size(x.left) + size(x.right);
    }

    // right rotate
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        return x;
    }

    // left rotate
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        return x;
    }
}
