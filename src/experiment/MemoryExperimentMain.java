package experiment;

import datastructures.PerfectHashTable;
import read.DataReader;
import read.ExperimentData;
import read.MalformedDataException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class MemoryExperimentMain {
    static final int MIN_EXPONENT = 10;
    static final int MAX_EXPONENT = 20;
    static final int FILE_AMOUNT = 10;
    static final String INPUT_FILE_TEMPLATE = "data-generation/uniform/u-2^%d-%d.data";
    static BiFunction<Integer, Integer, String> getFilePath =
            (exp, fileI) -> String.format(INPUT_FILE_TEMPLATE, exp, fileI);
    static final String OUTPUT_FOLDER = "output/family-uniform-memory/";



    public static void main(String[] args) {
        try {
            List<BiFunction<ExperimentData, Integer, Experiment>> experimentCreators = List.of(
                (data, exp) -> new BinaryTrieExperiment(data),
//                (data, exp) -> new BinaryPTrieExperiment(data),
//                (data, exp) -> new SplayTreeExperiment(data),
                (data, exp) -> new RedBlackTreeExperiment(data),
                (data, exp) -> new BitmapExperiment(data),
//                (data, exp) -> new SIBExperiment(data),
//                (data, exp) -> new vEBBitmapExperiment(data),
//                (data, exp) -> new vEBBitmapMinTreeExperiment(data),
                (data, exp) -> new vEBTreeExperiment(data),
//                (data, exp) -> new HashTableBitmapExperiment(data),
                ChainedDivisionHashTableExperiment::new
//                ABHashTableExperiment::new,
//                (data, exp) ->  new PerfectHashTableExperiment(data)
            );
            for (BiFunction<ExperimentData, Integer, Experiment> creator : experimentCreators) {
                doExperiment(creator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doExperiment(BiFunction<ExperimentData, Integer, Experiment> creator) throws MalformedDataException, IOException {
        int[] memoryUsages = new int[(MAX_EXPONENT - MIN_EXPONENT + 1) * FILE_AMOUNT];
        String name = null;
        for (int exp = MIN_EXPONENT; exp <= MAX_EXPONENT; exp++) {
            System.out.printf("Exponent: %d\n", exp);
            for (int fileIndex = 0; fileIndex < FILE_AMOUNT; fileIndex++) {
                System.out.printf("  fileIndex: %d\n", fileIndex);

                ExperimentData data = DataReader.read(getFilePath.apply(exp, fileIndex));

                Experiment e = creator.apply(data, exp);
                memoryUsages[fileIndex + (exp - MIN_EXPONENT) * FILE_AMOUNT] = e.runMemory();
                // this is inefficient but whatever.
                name = e.getOutputName();
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_FOLDER + name)));
        for (int i = 0; i < memoryUsages.length; i++) {
            if (i % FILE_AMOUNT == 0 && i != 0) {
                writer.write('\n');
            } else if (i != 0){
                writer.write(",");
            }
            writer.write(Integer.toString(memoryUsages[i]));

        }
        writer.flush();
    }
}
