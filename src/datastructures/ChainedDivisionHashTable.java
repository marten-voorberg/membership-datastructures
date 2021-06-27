package datastructures;

public class ChainedDivisionHashTable extends ChainedHashTable {
    private int exponent;

    public ChainedDivisionHashTable(int exponent) {
        super(1 << exponent);
        this.exponent = exponent;
    }


    @Override
    protected int hash(int x) {
        int shift = 32 - exponent;
        return (x << shift) >>> shift;
    }

    public static void main(String[] args) {
        ChainedDivisionHashTable ht = new ChainedDivisionHashTable(701);
        ht.insert(1);
        ht.insert(2);
        ht.insert(72);

        ht.isMember(1);
        ht.isMember(2);
        ht.isMember(72);

        System.out.println((int) (1024 * ((123456 * 0.618) % 1)));
    }
}
