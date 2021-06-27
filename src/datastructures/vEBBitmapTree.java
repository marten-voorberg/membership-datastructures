package datastructures;

import datastructures.Membership;

public class vEBBitmapTree implements Membership {
    private final int uExponent;
    private int shift;

    private vEBBitmapTree[] clusters;
    private int bitmap;

    private int high(int x) {
        return x >>> (uExponent >> 1);
    }

    private int low(int x) {
        return (x << shift) >>> shift;
    }

    public vEBBitmapTree(int uExponent) {
        this.uExponent = uExponent;
        if (uExponent == 4) {
            bitmap = 0;
        } else {
            this.shift = 32 - (uExponent >> 1);
            clusters = new vEBBitmapTree[1 << (uExponent >> 1)];
        }
    }

    @Override
    public boolean isMember(int x) {
        if (uExponent == 4) {
            return (bitmap & (1 << x)) != 0;
        } else {
            int h = high(x);
            return clusters[h] != null && clusters[h].isMember(low(x));
        }
    }

    @Override
    public void insert(int x) {
        if (uExponent == 4) {
            bitmap |= 1 << x;
        } else {
            int h = high(x);
            if (clusters[h] == null) {
                clusters[h] = new vEBBitmapTree(uExponent >> 1);
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
        vEBBitmapTree vEBBitmapTree = new vEBBitmapTree(32);
        vEBBitmapTree.insert(400);
        System.out.println(vEBBitmapTree.isMember(400));
        vEBBitmapTree.insert(1010101934);
        System.out.println(vEBBitmapTree.isMember(1010101934));
    }
}
