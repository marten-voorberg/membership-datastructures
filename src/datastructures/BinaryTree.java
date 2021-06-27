package datastructures;

public class BinaryTree implements Membership {
    private Node ROOT = null;
    private static final int WORD_SIZE = 32;

    class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    @Override
    public boolean isMember(int x) {
        return isMember(x, ROOT);
    }

    private boolean isMember(int x, Node node) {
        if (node == null) {
            return false;
        } else if (node.value == x) {
            return true;
        } else if (node.value < x) {
            return isMember(x, node.right);
        } else {
            return isMember(x, node.left);
        }
    }

    @Override
    public void insert(int x) {
        if (ROOT == null) {
            ROOT = new Node(x);
        } else {
            insert(x, ROOT);
        }
    }

    private void insert(int x, Node node) {
        if (node.value < x) {
            if (node.right == null) {
                node.right = new Node(x);
            } else {
                insert(x, node.right);
            }
        } else {
            if (node.left == null) {
                node.left = new Node(x);
            } else {
                insert(x, node.left);
            }
        }
    }

    @Override
    public void delete(int key) {
        if (!isMember(key)) {
            return;
        }

        delete(key, ROOT, null);
    }

    public void delete(int key, Node node, Node parent) {
        if (node == null) {
            return;
        }

        if (key < node.value) {
            delete(key, node.left, node);
        } else if (key > node.value) {
            delete(key, node.right, node);
        } else {
            if (node.left == null && node.right == null) {
                if (node == ROOT) {
                    ROOT = null;
                    return;
                }
                if (parent.left == node) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
            } else if (node.left == null) {
                node.value = node.right.value;;
                node.left = node.right.left;
                node.right = node.right.right;
            } else {
                node.value = node.left.value;;
                node.right = node.left.right;
                node.left = node.left.left;
            }
        }
    }
}
