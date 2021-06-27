package datastructures;

public class vEBBitmapMinTree implements Membership {
    private final int uExponent;
    private int shift;
    private static final int MIN_NULL = 0x8000000;
    private int min = MIN_NULL;

    private vEBBitmapMinTree[] clusters;
    private int bitmap;

    private int high(int x) {
        return x >>> (uExponent >> 1);
    }

    private int low(int x) {
        return (x << shift) >>> shift;
    }

    public vEBBitmapMinTree(int uExponent) {
        this.uExponent = uExponent;
        if (uExponent == 4) {
            bitmap = 0;
        } else {
            this.shift = 32 - (uExponent >> 1);
            clusters = new vEBBitmapMinTree[1 << (uExponent >> 1)];
        }
    }

    @Override
    public boolean isMember(int x) {
        if (x == min) {
            return true;
        } else if (uExponent == 4) {
            return (bitmap & (1 << x)) != 0;
        } else {
            int h = high(x);
            return clusters[h] != null && clusters[h].isMember(low(x));
        }
    }

    @Override
    public void insert(int x) {
        if (min == MIN_NULL) {
            min = x;
            return;
        }

        if (x < min) {
            int oldMin = min;
            min = x;
            x = oldMin;
        }

        if (uExponent == 4) {
            bitmap |= 1 << x;
        } else {
            int h = high(x);
            if (clusters[h] == null) {
                clusters[h] = new vEBBitmapMinTree(uExponent >> 1);
            }
            clusters[h].insert(low(x));
        }
    }

    @Override
    public void delete(int x) {
        if (uExponent == 4) {
            bitmap &= ~(1 << x);
        } else {
            int h = high(x);
            if (clusters[h] != null) {
                clusters[h].delete(low(x));
            }
        }
    }

    public static void main(String[] args) {
        vEBBitmapMinTree vEBBitmapTree = new vEBBitmapMinTree(32);
        vEBBitmapTree.insert(400);
        System.out.println(vEBBitmapTree.isMember(400));
        vEBBitmapTree.insert(1010101934);
        System.out.println(vEBBitmapTree.isMember(1010101934));
    }
}
