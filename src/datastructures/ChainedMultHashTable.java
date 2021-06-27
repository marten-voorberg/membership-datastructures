package datastructures;

import java.math.BigInteger;

public class ChainedMultHashTable extends ChainedHashTable {

    public ChainedMultHashTable(int size) {
        super(size);

    }

    @Override
    protected int hash(int k) {
        final float A = 0.618f;
        k = 0x7fffffff & k;
        double kA = k * A;
        System.out.println(kA);
        System.out.println((int) kA);
        int res = (int) (size * (kA - ((int) kA)));
        System.out.println(res);
        return res;
    }

    public static void main(String[] args) {
        Membership m = new ChainedMultHashTable(64);
        m.insert(100);
    }
}
