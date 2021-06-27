package datastructures;

public class vEBTree implements Membership {
    private boolean isEmpty = true;
    private final int uExponent;
    private final int shift;
    private vEBTree summary;

    private int min = 0;
    private int max = 0;

    private final vEBTree[] clusters;

    private int high(int x) {
        return x >>> (uExponent >> 1);
    }

    private int low(int x) {
        return (x << shift) >>> shift;
    }

    private int index(int x, int y) {
        return (x << (uExponent >>> 1)) + y;
    }

    public vEBTree(int uExponent) {
        this.uExponent = uExponent;
        this.shift = 32 - (uExponent >> 1);
        clusters = new vEBTree[1 << (uExponent >> 1)];
        if (uExponent != 1) {
            summary = new vEBTree(uExponent >> 1);
        }
    }

    @Override
    public boolean isMember(int x) {
        if (isEmpty) {
            return false;
        } else if (x == min || x == max) {
            return true;
        } else if (uExponent == 1) {
            return false;
        } else {
            int h = high(x);
            return clusters[h] != null && clusters[h].isMember(low(x));
        }
    }

    @Override
    public void insert(int x) {
        if (isEmpty) {
            min = x;
            max = x;
            isEmpty = false;
        } else {
            if (x < min) {
                int temp = min;
                min = x;
                x = temp;
            }

            if (uExponent > 1) {
                int h = high(x);
                if (clusters[h] == null) {
                    clusters[h] = new vEBTree(uExponent >> 1);
                    summary.insert(h);
                }
                clusters[h].insert(low(x));
            }

            if (x > max) {
                max = x;
            }
        }
    }

    @Override
    public void delete(int x) {
        if (!isMember(x)) {
            return;
        }

        if (min == max) {
            isEmpty = true;
        } else if (uExponent == 1) {
            if (x == 0) {
                min = 1;
            } else {
                min = 0;
            }
            max = min;
        } else {
            if (x == min) {
                int clusterIndex = summary.min;
//                for (int i = 0; i < clusters.length; i++) {
//                    if (clusters[i] != null) {
//                        clusterIndex = i;
//                        break;
//                    }
//                }
                x = index(clusterIndex, clusters[clusterIndex].min);
                min = x;
            }

            clusters[high(x)].delete(low(x));

            if (clusters[high(x)].isEmpty) {
                summary.delete(high(x));
                if (x == max) {
                    int summaryMax = summary.max;

                    if (summary.isEmpty) {
                        max = min;
                    } else {
                        max = index(summaryMax, clusters[summaryMax].max);
                    }
                }
            } else if (x == max) {
                max = index(high(x), clusters[high(x)].max);
            }
        }
    }

    public static void main(String[] args) {
        vEBTree vEBTree = new vEBTree(32);
        vEBTree.insert(400);
        System.out.println(vEBTree.isMember(400));
        vEBTree.delete(400);
        System.out.println(vEBTree.isMember(400));

//        System.out.println(vEBTree.isMember(400));
//        vEBTree.insert(1010101934);
//        System.out.println(vEBTree.isMember(1010101934));
    }
}
