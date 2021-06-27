package datastructures;

public class Bitmap implements Membership {
    private final int[] BITMAP = new int[(0xffffffff >>> 5) + 1];

    @Override
    public boolean isMember(int x) {
        return ((BITMAP[x >>> 5]) & (1 << (x & 0b11111))) != 0;
    }

    @Override
    public void insert(int x) {
        BITMAP[x >>> 5] = (BITMAP[x >>> 5]) | (1 << (x & 0b11111));
    }

    @Override
    public void delete(int x) {
        BITMAP[x >>> 5] = (BITMAP[x >>> 5]) & (~(1 << (x & 0b11111)));
    }
}
