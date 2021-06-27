package datastructures;

import read.DataReader;
import read.ExperimentData;
import read.MalformedDataException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HashTableBitmap extends ChainedDivisionHashTable {
    public int[] lowerBounds;
    public int[] upperBounds;
    public int[][] bitmaps;
    public int pi;
    public int partitionSize;
    public int partitionAmount;

    public static double SPARSENESS_THRESHOLD = 0.0025;

    public HashTableBitmap(List<Integer> input) {
        super((int) Math.ceil(Math.log(input.size()) / Math.log(2)));
        Collections.sort(input);


        pi = -1;
        int bestCount = -1;

        for (int piPrime = 2; piPrime <= 5; piPrime++) {
            int index = 0;
            int count = 0;
            int partitionSize = 0x80000000 >>> (piPrime - 1);
            int partitionAmount = 1 << piPrime;

            int[][] bitmaps = new int[partitionAmount][];

            for (int p = 0; p < partitionAmount; p++) {
                int[] bitmap = new int[(partitionSize >>> 5) + 1] ;
                if (index >= input.size()) {
                    break;
                }

                int partitionCount = 0;

                int lowerBound = Integer.MIN_VALUE + p * partitionSize;
                int upperBound = Integer.MIN_VALUE + (1 + p) * partitionSize;
                if (p == partitionAmount - 1) {
                    upperBound--;
                }

                while (input.size() > index && input.get(index) > lowerBound && input.get(index) <= upperBound) {
                    int x = input.get(index);
                    x -= lowerBound;
                    if (x < 0) {
                        x += partitionSize;
                    }
                    bitmap[x >>> 5] |= 1 << (x & 0b11111);

                    index++;
                    partitionCount++;
                }

                if (((double)partitionCount / (double) partitionSize) >= SPARSENESS_THRESHOLD) {
                    count += partitionCount;
                    bitmaps[p] = bitmap;
                }
            }

            if (count >= bestCount) {
                bestCount = count;
                pi = piPrime;
                this.bitmaps = bitmaps;
            }
        }

        partitionSize = 0x80000000 >>> (pi - 1);
        partitionAmount = 1 << pi;

        for (int x : input) {
            if (!isMember(x)) {
                insert(x);
            }
        }
    }

    @Override
    public boolean isMember(int x) {
        int p = partition(x);
        if (bitmaps[p] != null) {
            x -= lowerBound(p);
            if (x < 0) {
                x += partitionSize;
            }
            return ((bitmaps[p][x >>> 5]) & (1 << (x & 0b11111))) != 0;
        } else {
            return super.isMember(x);
        }
    }

    @Override
    public void insert(int x) {
        int p = partition(x);
        if (bitmaps[p] != null) {
            x -= lowerBound(p);
            if (x < 0) {
                x += partitionSize;
            }
            bitmaps[p][x >>> 5] |= 1 << (x & 0b11111);
        } else {
            super.insert(x);
        }
    }


    @Override
    public void delete(int x) {
        int p = partition(x);
        if (bitmaps[p] != null) {
            x -= lowerBound(p);
            if (x < 0) {
                x += partitionSize;
            }
            bitmaps[p][x >>> 5] &= (~(1 << (x & 0b11111)));
        } else {
            super.insert(x);
        }
    }

    private int partition(int x) {
        return x / partitionSize + (partitionAmount / 2);
    }

    private int lowerBound(int partition) {
        return Integer.MIN_VALUE + partition * partitionSize;
    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("test/datastructures/data/test-multiple-normal-insert.data")));

            int[] ints = reader.lines().mapToInt(Integer::valueOf).toArray();
            List<Integer> input = new ArrayList<>();
            for (int i = 0; i < ints.length; i++) {
                input.add(ints[i]);
            }
            input.add(Integer.MAX_VALUE - 1000);

            HashTableBitmap htb = new HashTableBitmap(input);

            System.out.printf("inList: %b, inHTB: %b\n", input.contains(3810), htb.isMember(3810));
            System.out.printf("inList: %b, inHTB: %b\n", input.contains(10000), htb.isMember(10000));
            System.out.printf("inList: %b, inHTB: %b\n", input.contains(Integer.MAX_VALUE - 1000), htb.isMember(Integer.MAX_VALUE - 1000));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(Integer.MAX_VALUE);
//
//        for (int p = 2; p <= 5; p++) {
//            System.out.printf("p=%d\n", p);
//            int partitionSize = 0x80000000 >>> (p - 1);
//            for (int pIndex = 0; pIndex < (1 << p); pIndex++) {
//                int lowerBound = Integer.MIN_VALUE + pIndex * partitionSize;
//                int upperBound = Integer.MIN_VALUE + (1 + pIndex) * partitionSize;
//                if (pIndex == (1 << p) - 1) {
//                    upperBound--;
//                }
//                System.out.printf("  Interval: (%d, %d)\n", lowerBound, upperBound);
//            }
//        }
    }
}
