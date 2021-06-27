package datastructures;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ABHashTable extends ChainedHashTable {
    private int p;
    private int a;
    private int b;

    public ABHashTable(int size) {
        super(size);

        for (int possibleP = size; possibleP < 2 * possibleP; possibleP++) {
            if (isPrime(possibleP)) {
                p = possibleP;
                break;
            }
        }

        a = getUniformRandom();
        b = getUniformRandom();
    }

    @Override
    protected int hash(int k) {
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
