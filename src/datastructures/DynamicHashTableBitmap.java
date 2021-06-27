package datastructures;

import java.util.Arrays;

public class DynamicHashTableBitmap implements Membership {
    public static final int PI = 5;
    public static final int PARTITION_AMOUNT = 1 << PI;
    public static final int PARTITION_SIZE = 0x80000000 >>> (PI - 1);
    public static final int INSERT_THRESHOLD = 20000;
    public static final int DELETE_THRESHOLD = INSERT_THRESHOLD / 2;

    private static class Node {
        public int value;

        public Node bucketNext;
        public Node bucketPrev;

        public Node partitionNext;
        public Node partitionPrev;

        public Node(int x) {
            this.value = x;
        }

        public Node() {
            this(0);
        }

        public static Node sentinel() {
            Node result = new Node();
            result.bucketNext = result;
            result.bucketPrev = result;
            result.partitionNext = result;
            result.partitionPrev = result;
            return result;
        }
    }

    private final Node[] list;
    public int[][] bitmaps;
    public int[] partitionSizes;
    public Node[] partitionHeads;
    private int sizeExponent;
    private int size;

    public DynamicHashTableBitmap(int sizeExponent) {
        this.sizeExponent = sizeExponent;
        this.size = 1 << sizeExponent;

        this.list = new Node[size];

        this.partitionSizes = new int[PARTITION_AMOUNT];
        this.bitmaps = new int[PARTITION_AMOUNT][];
        this.partitionHeads = new Node[PARTITION_AMOUNT];
        for (int i = 0; i < PARTITION_AMOUNT; i++) {
            partitionHeads[i] = new Node();
        }
    }

    private int hash(int x) {
        int shift = 32 - sizeExponent;
        return (x << shift) >>> shift;
    }

    private int partition(int x) {
        return x / PARTITION_SIZE + (PARTITION_AMOUNT / 2);
    }

    private int lowerBound(int partition) {
        return Integer.MIN_VALUE + partition * PARTITION_SIZE;
    }

    @Override
    public boolean isMember(int x) {
        int partition = partition(x);

        if (bitmaps[partition] != null) {
            x -= lowerBound(partition);
            if (x < 0) {
                x += PARTITION_SIZE;
            }
            return ((bitmaps[partition][x >>> 5]) & (1 << (x & 0b11111))) != 0;
        } else {
            int key = hash(x);
            Node current = list[key];

            while (current != null) {
                if (current.value == x) {
                    return true;
                }

                current = current.bucketNext;
            }
            return false;
        }
    }



    @Override
    public void insert(int x) {
        if (isMember(x)) {
            return;
        }

//        int partition = Math.floorDiv(x, PARTITION_SIZE);
        int partition = partition(x);
        this.partitionSizes[partition]++;


        if (bitmaps[partition] == null && partitionSizes[partition] >= INSERT_THRESHOLD) {
//            System.out.printf("Creating bitmap for p=%d\n", partition);
            bitmaps[partition] = new int[PARTITION_SIZE >>> 5];
            Node current = partitionHeads[partition].partitionNext;
            while (current != null) {
                int y = current.value - lowerBound(partition);
                if (y < 0) {
                    y += PARTITION_SIZE;
                }
                bitmaps[partition][y >>> 5] |= (1 << (y & 0b11111));

                current.partitionPrev.partitionNext = current.partitionNext;
                if (current.partitionNext != null) {
                    current.partitionNext.partitionPrev = current.partitionPrev;
                }

                current = current.partitionNext;
            }
            x -= lowerBound(partition);
            if (x < 0) {
                 x += PARTITION_SIZE;
            }
            bitmaps[partition][x >>> 5] |= (1 << (x & 0b11111));

//            System.out.printf("Created bitmap for partition %d\n", partition);
        } if (bitmaps[partition] != null) {
            int remainder = Math.floorMod(x, PARTITION_SIZE);
            bitmaps[partition][remainder >>> 5] |= (1 << (remainder & 0b11111));
        } else {
            hashTableInsert(x);
        }
    }

    private void hashTableInsert(int x) {
        int key = hash(x);
        Node current = list[key];
        Node newNode = new Node(x);

        // partition dll insert
        Node head = partitionHeads[partition(x)];
        newNode.partitionNext = head.partitionNext;
        head.partitionNext = newNode;
        newNode.partitionPrev = head;
        if (newNode.partitionNext != null) {
            newNode.partitionNext.partitionPrev = newNode;
        }

        if (current == null) {
            list[key] = newNode;
        } else {
            while (current.bucketNext != null) {
                current = current.bucketNext;
            }

            current.bucketNext = newNode;
            newNode.bucketPrev = current;
        }
    }

    @Override
    public void delete(int x) {
        if (!isMember(x)) {
            return;
        }

        int partition = partition(x);
        this.partitionSizes[partition]--;



        if (bitmaps[partition] != null) {
            if (partitionSizes[partition] <= DELETE_THRESHOLD) {
//                System.out.println("Deleting bitmap");
                int[] bitmap = bitmaps[partition];
                int lowerBound = lowerBound(partition);

                for (int bitmapIndex = 0; bitmapIndex < bitmap.length; bitmapIndex++) {
                    if (bitmap[bitmapIndex] != 0) {
                        int number = bitmap[bitmapIndex];
                        for (int byteSet = 0; byteSet < 32; byteSet++) {
                            if ((number & (1 << byteSet)) != 0) {
                                int elem = lowerBound + (bitmapIndex << 5) + byteSet;
                                hashTableInsert(elem);
                            }
                        }
                    }
                }

                bitmaps[partition] = null;
                hashTableDelete(x);
            } else {
                x -= lowerBound(partition);
                if (x < 0) {
                    x += PARTITION_SIZE;
                }
                bitmaps[partition][x >>> 5] &= ~(1 << (x & 0b11111));
            }
        } else {
            hashTableDelete(x);
        }
    }

    private void hashTableDelete(int x) {
        int key = hash(x);
        Node current = list[key];

        if (current != null && current.value == x) {
            list[key] = current.bucketNext;

            if (current.partitionPrev != null) {
                current.partitionPrev.partitionNext = current.partitionNext;
            }

            if (current.partitionNext != null) {
                current.partitionNext.partitionPrev = current.partitionPrev;
            }
        }

        while (current != null) {
            if (current.value == x) {
                if (current.bucketPrev != null) {
                    current.bucketPrev.bucketNext = current.bucketNext;
                }

                if (current.bucketNext != null) {
                    current.bucketNext.bucketPrev = current.bucketPrev;
                }

                if (current.partitionPrev != null) {
                    current.partitionPrev.partitionNext = current.partitionNext;
                }

                if (current.partitionNext != null) {
                    current.partitionNext.partitionPrev = current.partitionPrev;
                }

                return;
            }

            current = current.bucketNext;
        }
    }

    public static void main(String[] args) {
        DynamicHashTableBitmap dhtb = new DynamicHashTableBitmap(10);
        dhtb.insert(100);
        System.out.println(dhtb.isMember(100));
        System.out.println(Arrays.toString(dhtb.partitionSizes));
    }
}