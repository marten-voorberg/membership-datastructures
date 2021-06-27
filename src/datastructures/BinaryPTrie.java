package datastructures;

public class BinaryPTrie implements Membership {
    static class Node {
        Node left;
        Node right;

        int leftPrefix;
        int leftPrefixLength;
        int rightPrefix;
        int rightPrefixLength;
    }

    Node root;

    public BinaryPTrie(BinaryTrie2 trie) {
        this.root = toPTrie(trie.ROOT);
    }

    private Node toPTrie(BinaryTrie2.Node trieNode) {
        Node node = new Node();

        if (trieNode.left != null) {
            int prefixLength = 1;
            int prefix = 0;

            BinaryTrie2.Node current = trieNode.left;
            while (current.right == null ^ current.left == null) {
                prefixLength++;
                prefix <<= 1;
                if (current.right != null) {
                    prefix |= 1;
                    current = current.right;
                } else {
                    current = current.left;
                }
            }

            node.leftPrefixLength = prefixLength;
            node.leftPrefix = prefix;
            node.left = toPTrie(current);
        }

        if (trieNode.right != null) {
            int prefixLength = 1;
            int prefix = 1;

            BinaryTrie2.Node current = trieNode.right;
            while (current.right == null ^ current.left == null) {
                prefixLength++;
                prefix <<= 1;
                if (current.right != null) {
                    prefix |= 1;
                    current = current.right;
                } else {
                    current = current.left;
                }
            }

            node.rightPrefixLength = prefixLength;
            node.rightPrefix = prefix;
            node.right = toPTrie(current);
        }

        return node;
    }

    public BinaryPTrie() {
        root = new Node();
        root.leftPrefix = 0b00;
        root.leftPrefixLength = 2;
        root.rightPrefix = 0b10;
        root.rightPrefixLength = 2;

        Node left = new Node();
        left.leftPrefixLength = 30;
        left.left = new Node();
        root.left  = left;

        Node right = new Node();
        right.leftPrefix = 0;
        right.leftPrefixLength = 1;
        right.rightPrefix = 1;
        right.rightPrefixLength = 1;
        root.right = right;

        Node rightRight = new Node();
        rightRight.leftPrefixLength = 29;
        rightRight.left = new Node();
        right.right = rightRight;


        Node rightLeft = new Node();
        rightLeft.leftPrefixLength = 29;
        rightLeft.left = new Node();

        right.left = rightLeft;
    }

    @Override
    public boolean isMember(int x) {
        Node n = root;
        int shiftRemainder = 32;
        while (true) {
            int shift = shiftRemainder - n.leftPrefixLength;
            int s = n.leftPrefixLength + (32 - shiftRemainder);

            if (n.left != null && (x >>> shift == n.leftPrefix)) {
                x = x & (0xffffffff << s) >>> s;
                n = n.left;
                shiftRemainder = shift;
                continue;
            }

            shift = shiftRemainder - n.rightPrefixLength;
            s = n.rightPrefixLength + (32 - shiftRemainder);

            if (n.right != null && (x >>> shift == n.rightPrefix)){
                x = x & (0xffffffff << s) >>> s;
                n = n.right;
                shiftRemainder = shift;
            } else {
                break;
            }
        }

        return shiftRemainder == 0;
    }

//        if (n.left != null) {
//            int shift = shiftRemainder - n.leftPrefixLength;
//            if (x >>> shift == n.leftPrefix) {
//                int s = n.leftPrefixLength + (32 - shiftRemainder);
//                return isMember(x & (0xffffffff << s) >>> s, n.left, shift);
//            }
//        }
//
//        if (n.right != null) {
//            int shift = shiftRemainder - n.rightPrefixLength;
//            if (x >>> shift == n.rightPrefix) {
//                int s = n.rightPrefixLength + (32 - shiftRemainder);
//                return isMember(x & (0xffffffff << s) >>> s, n.right, shift);
//            }
//        }
//
//        return shiftRemainder == 0;
//    }

    @Override
    public void insert(int x) {

    }

    @Override
    public void delete(int x) {
        // untested
        Node n = root;
        Node prev = null;
        boolean left = false;
        int shiftRemainder = 32;
        while (true) {
            int shift = shiftRemainder - n.leftPrefixLength;
            int s = n.leftPrefixLength + (32 - shiftRemainder);

            if (n.left != null && (x >>> shift == n.leftPrefix)) {
                x = x & (0xffffffff << s) >>> s;
                prev = n;
                n = n.left;
                left = true;
                shiftRemainder = shift;
                continue;
            }

            shift = shiftRemainder - n.rightPrefixLength;
            s = n.rightPrefixLength + (32 - shiftRemainder);

            if (n.right != null && (x >>> shift == n.rightPrefix)) {
                x = x & (0xffffffff << s) >>> s;
                prev = n;
                n = n.right;
                left = false;
                shiftRemainder = shift;
            } else {
                break;
            }
        }

        if (shiftRemainder == 0) {
            if(left) {
                prev.left = null;
            } else {
                prev.right = null;
            }
        }
    }

    public static void main(String[] args) {
//        BinaryPTrie bpt = new BinaryPTrie();
//        for (int i = 0; i < 16; i++) {
//            System.out.printf("%d - isMember(%d << 28)=%b\n", i, i, bpt.isMember(i << 28));
//        }
//
//        System.out.println(bpt.isMember(0b101 << 29));

        BinaryTrie2 trie = new BinaryTrie2();
//        trie.insert(0);
//        trie.insert(10);
//        trie.insert(13);
        trie.insert(0xa << 28);
        trie.insert(0x8 << 28);
        trie.insert(0);

        BinaryPTrie bpt = new BinaryPTrie(trie);
        for (int i = 0; i < 16; i++) {
            System.out.printf("%d - isMember(%d << 28)=%b\n", i, i, bpt.isMember(i << 28));
        }

        System.out.println(bpt.isMember(0x8 << 28));
    }
}
