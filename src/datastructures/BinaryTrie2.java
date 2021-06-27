package datastructures;

public class BinaryTrie2 implements Membership {
    public final Node ROOT = new Node();
    public static class Node {
        public Node left;
        public Node right;
    }

    @Override
    public boolean isMember(int x) {
        Node current = ROOT;
        for (int i = 31; i >= 0; i--) {
            int relevantBit = x & (1 << i);

            if (relevantBit != 0) {
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
        }

        return true;
    }

    @Override
    public void insert(int x) {
        Node current = ROOT;

        for (int i = 31; i >= 0; i--) {
            int relevantBit = x & (1 << i);

            if (relevantBit != 0) {
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
        }
    }

    @Override
    public void delete(int x) {
        Node current = ROOT;
        for (int i = 31; i >= 0; i--) {
            int relevantBit = x & (1 << i);

            if (relevantBit != 0) {
                if (current.right == null) {
                    return;
                } else if (i == 1) {
                    current.right = null;
                    return;
                }
                current = current.right;
            } else {
                if (current.left == null) {
                    return;
                } else if (i == 1) {
                    current.left = null;
                    return;
                }
                current = current.left;
            }
        }
    }
}
