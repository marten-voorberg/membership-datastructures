package datastructures;

import java.util.concurrent.ThreadLocalRandom;

public class ABHashTableRBT implements Membership {
    private int p;
    private int a;
    private int b;
    private int size;
    private RedBlackTree[] table;

    public ABHashTableRBT(int size) {
        this.size = size;

        for (int possibleP = size; possibleP < 2 * possibleP; possibleP++) {
            if (isPrime(possibleP)) {
                p = possibleP;
                break;
            }
        }

        a = getUniformRandom();
        b = getUniformRandom();

        table = new RedBlackTree[size];
    }

    @Override
    public boolean isMember(int x) {
        int h = hash(x);
        return table[h] != null && table[h].isMember(x);
    }

    @Override
    public void insert(int x) {
        int h = hash(x);
        if (table[h] == null) {
            table[h] = new RedBlackTree();
        }
        table[h].insert(x);
    }

    @Override
    public void delete(int x) {
        int h = hash(x);
        if (table[h] != null) {
            table[h].delete(x);
        }
    }

    private int hash(int k) {
        return Math.floorMod(Math.floorMod(a * k + b, p), size);
    }

    private static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private int getUniformRandom() {
        return ThreadLocalRandom.current().nextInt(0, size + 1);
    }
}
