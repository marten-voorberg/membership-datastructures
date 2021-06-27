package datastructures;

import java.util.concurrent.ThreadLocalRandom;

public class OpenAddressingHashTable implements Membership {
    private int p;
    private int m;
    private int a;
    private int b;

    private boolean hasZero = false;

    private int[] table;

    public OpenAddressingHashTable(int m) {
        this.m = m;
        this.table = new int[m];

        for (int possibleP = m; possibleP < 2 * possibleP; possibleP++) {
            if (isPrime(possibleP)) {
                p = possibleP;
                break;
            }
        }

        a = getUniformRandom();
        b = getUniformRandom();
    }

    @Override
    public boolean isMember(int x) {
        if (x == 0) {
            return hasZero;
        }

        int i = 0;
        int j;
        do {
            j = hash(x, i);
            if (table[j] == x) {
                return true;
            }
            i++;
        } while (table[j] != 0 && i != m);

        return false;
    }

    @Override
    public void insert(int x) {
        if (x == 0) {
            hasZero = true;
        }

        if (isMember(x)) {
            return;
        }

        int i = 0;
        int j;
        do {
            j = hash(x, i);
            i++;
        } while (table[j] != 0 && i != m);

        if (table[j] == 0) {
            table[j] = x;
        }
    }

    @Override
    public void delete(int x) {
        if (x == 0) {
            hasZero = false;
        }

        throw new UnsupportedOperationException();
    }

    private int hash(int k, int i) {
        return Math.floorMod(Math.floorMod(Math.floorMod(a * k + b, p), m) + i, m);
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
        return ThreadLocalRandom.current().nextInt(0, m + 1);
    }
}

