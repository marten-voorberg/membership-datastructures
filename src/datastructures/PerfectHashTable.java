package datastructures;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PerfectHashTable implements Membership {
    private int m;
    private int p;
    private int outerA;
    private int outerB;

    private int[] innerAs;
    private int[] innerBs;
    private int[] innerPs;
    private Integer[][] innerHTs;

    public PerfectHashTable(Set<Integer> set) {
        // Set p to the first prime bigger than m.
        m = set.size() * 2;
        innerAs = new int[m];
        innerBs = new int[m];
        innerPs = new int[m];
        innerHTs = new Integer[m][];

        p = getClosestPrime(m);

        int squaredSum;
        Set<Integer>[] groupedValues;
        do {
            outerA = getUniformRandom();
            outerB = getUniformRandom();
            groupedValues = new Set[m];

            for (int x : set) {
                int h = hash(x);
                if (groupedValues[h] == null) {
                    groupedValues[h] = new HashSet<>();
                }
                groupedValues[h].add(x);
            }

            squaredSum = 0;
            for (int i = 0; i < m; i++) {
                int lj = groupedValues[i] == null ? 0 : groupedValues[i].size();
                squaredSum += lj * lj;
            }
        } while (squaredSum > 2 * m);

        for (int i = 0; i < groupedValues.length; i++) {
            Set<Integer> group = groupedValues[i];
            if (group == null) {
                continue;
            }

            int addr = hash((Integer) group.toArray()[0]);
            int squaredLength = group.size() * group.size();
            boolean collision;
            do {
                collision = false;
                innerAs[addr] = getUniformRandom();
                innerBs[addr] = getUniformRandom();
                innerPs[addr] = getClosestPrime(squaredLength);
                innerHTs[addr] = new Integer[squaredLength];

                for (int x : group) {
                    int h = hash(x, addr);
                    if (innerHTs[addr][h] != null) {
                        collision = true;
                        break;
                    }

                    innerHTs[addr][h] = x;
                }
            } while (collision);
        }
    }

    private int hash(int k) {
        return Math.floorMod(Math.floorMod(outerA * k + outerB, p), m);
    }

    private int hash(int k, int i) {
        return Math.floorMod(Math.floorMod(innerAs[i] * k + innerBs[i], innerPs[i]), innerHTs[i].length);
    }

    @Override
    public boolean isMember(int x) {
        int outerKey = hash(x);
        Integer[] innerHT = innerHTs[outerKey];

        if (innerHT == null) {
            return false;
        } else {
            int innerKey = hash(x, outerKey);
            return innerHT[innerKey] != null && innerHT[innerKey] == x;
        }
    }

    @Override
    public void insert(int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int x) {
        throw new UnsupportedOperationException();
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

    private int getClosestPrime(int n) {
        for (int possibleP = n; possibleP < 2 * possibleP; possibleP++) {
            if (isPrime(possibleP)) {
                return possibleP;
            }
        }

        return -1;
    }

    private int getUniformRandom() {
        return ThreadLocalRandom.current().nextInt(0, m + 1);
    }

    public int getByteSize() {
        int result = 0;
        result += 12 * innerAs.length; // 3 innerAs, innerBs, innerPs. each int is 4 bytes
        result += 4 * innerHTs.length;

        for (int i = 0; i < innerHTs.length; i++) {
            if (innerHTs[i] != null) {
                result += innerHTs[i].length * 4;
            }
        }

        return result;
    }
}
