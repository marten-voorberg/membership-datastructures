package datastructures;

public class LinkedList implements Membership {
    private final Node SENTINEL = new Node(0, null);
    private Node head = SENTINEL;

    class Node {
        public int value;
        public Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public boolean isMember(int x) {
        Node current = this.SENTINEL;

        while (current.next != null) {
            current = current.next;
            if (current.value == x) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void insert(int x) {
        Node newNode = new Node(x, null);
        this.head.next = newNode;
        this.head = newNode;
    }

    @Override
    public void delete(int x) {
        Node current = SENTINEL;

        while (current.next != null) {
            if (current.next.value == x) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }
}
