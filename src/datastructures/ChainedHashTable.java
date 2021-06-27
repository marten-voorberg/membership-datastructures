package datastructures;

public abstract class ChainedHashTable implements Membership {
    protected int size;
    private class Node {
        public int value;
        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    protected abstract int hash(int x);

    private final Node[] LIST;

    public ChainedHashTable(int size) {
        this.size = size;
        this.LIST = new Node[size];
    }

    @Override
    public boolean isMember(int x) {
        int key = hash(x);
        Node bucket = LIST[key];

        if (bucket != null) {
            do {
                if (bucket.value == x) {
                    return true;
                }

                bucket = bucket.next;
            } while (bucket != null);

        }
        return false;
    }

    @Override
    public void insert(int x) {
        int key = hash(x);
        Node bucket = LIST[key];

        if (bucket == null) {
            LIST[key] = new Node(x);
        } else {
            while (bucket.next != null) {
                bucket = bucket.next;
            }
            bucket.next = new Node(x);
        }
    }

    @Override
    public void delete(int x) {
        int key = hash(x);
        Node bucket = LIST[key];

        if (bucket != null) {
            if (bucket.value == x) {
                LIST[key] = bucket.next;
            } else {
                while (bucket != null && bucket.next != null) {
                    if (bucket.next.value == x) {
                        bucket.next = bucket.next.next;
                    }

                    bucket = bucket.next;
                }
            }
        }
    }
}
