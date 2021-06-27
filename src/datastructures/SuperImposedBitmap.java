package datastructures;

public class SuperImposedBitmap implements Membership {
    SIBNode left = new SIBNode(5);
    SIBNode midLeft = new SIBNode(5);
    SIBNode midRight = new SIBNode(5);
    SIBNode right = new SIBNode(5);


    @Override
    public boolean isMember(int x) {
        int newX = x & 0x3fffffff;
        int addr = x >>> 30;
        if (addr == 0b11) {
            return left.isMember(newX);
        } else if (addr == 0b10) {
            return midLeft.isMember(newX);
        } else if (addr == 0b01) {
            return midRight.isMember(x);
        } else {
            return right.isMember(x);
        }
    }

    @Override
    public void insert(int x) {
        int newX = x & 0x3fffffff;
        int addr = x >>> 30;
        if (addr == 0b11) {
            left.insert(newX);
        } else if (addr == 0b10) {
            midLeft.insert(newX);
        } else if (addr == 0b01) {
            midRight.insert(newX);
        } else {
            right.insert(newX);
        }
    }

    @Override
    public void delete(int x) {
        int newX = x & 0x3fffffff;
        int addr = x >>> 30;
        if (addr == 0b11) {
            left.delete(newX);
        } else if (addr == 0b10) {
            midLeft.delete(newX);
        } else if (addr == 0b01) {
            midRight.delete(newX);
        } else {
            right.delete(newX);
        }
    }
}

class SIBNode {
    int height;
    SIBNode[] children;
    int value;

    public SIBNode(int height) {
        this.height = height;

        if (height != 0) {
            this.children = new SIBNode[32];
        }
    }

    public void insert(int x) {
        if (height == 0) {
            value = value | (1 << x);
        } else {
            int addr = (x >>> (5* height)) & 0b11111;
            int newX = x & ~(0b11111 << (5* height));
            if (children[addr] == null) {
                children[addr] = new SIBNode(height - 1);
            }

            children[addr].insert(newX);
        }
    }

    public void delete(int x) {
        if (height == 0) {
            value &= (~(1 << x));
        } else {
            int addr = (x >>> (5* height)) & 0b11111;
            int newX = x & ~(0b11111 << (5* height));
            if (children[addr] == null) {
                return;
            }

            children[addr].delete(newX);
        }
    }

    public boolean isMember(int x) {
        if (height == 0) {
            return (value & (1 << x)) != 0;
        } else {
            int addr = (x >>> (5* height)) & 0b11111;
            int newX = x & ~(0b11111 << (5* height));
            return children[addr] != null && children[addr].isMember(newX);
        }
    }
}