package datastructures;

public class BinaryTrie implements Membership {
    public final Node ROOT = new Node();
    public static class Node {
        public Node left;
        public Node right;
    }

    @Override
    public boolean isMember(int x) {
        Node current = ROOT;
        for (int depth = 0; depth < 32; depth++) {
            if ((x & 1) == 1) {
                if (current.right == null) {
                    return false;
                }
                current = current.right;
            } else {
                if (current.left == null) {
                    return false;
                }
                current = current.left;
            }
            x = x >> 1;
        }

        return true;
    }

    @Override
    public void insert(int x) {
        Node current = ROOT;
        for (int depth = 0; depth < 32; depth++) {
            if ((x & 1) == 1) {
                if (current.right == null) {
                    current.right = new Node();
                }
                current = current.right;
            } else {
                if (current.left == null) {
                    current.left = new Node();
                }
                current = current.left;
            }

            x = x >> 1;
        }
    }

    @Override
    public void delete(int x) {
        Node current = ROOT;
        for (int depth = 0; depth < 32; depth++) {
            if ((x & 1) == 1) {
                if (current.right == null) {
                    return;
                } else if (depth == 30) {
                    current.right = null;
                    return;
                }
                current = current.right;
            } else {
                if (current.left == null) {
                    return;
                } else if (depth == 30) {
                    current.left = null;
                    return;
                }
                current = current.left;
            }
            x = x >> 1;
        }
    }
}
