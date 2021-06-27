package experiment;

import read.DataReader;
import read.ExperimentData;

import java.util.List;
import java.util.function.BiFunction;

public class TimedExperimentMain {
    public static void main(String[] args) {
        final int LOWEST_EXPONENT = 10;
        final int HIGHEST_EXPONENT = 20;
        final int N_EXPERIMENTS = 10;
        final int N_FILES = 1;

        final String baseOutputFolder = "output/family-normal-static";

        List<BiFunction<ExperimentData, Integer, Experiment>> experimentCreators = List.of(
                (data, exp) -> new BinaryTrieExperiment(data),
//                (data, exp) -> new BinaryPTrieExperiment(data),
////                (data, exp) -> new SplayTreeExperiment(data),
                (data, exp) -> new RedBlackTreeExperiment(data),
                (data, exp) -> new BitmapExperiment(data),
////                (data, exp) -> new SIBExperiment(data),
//                (data, exp) -> new vEBBitmapExperiment(data),
                (data, exp) -> new vEBTreeExperiment(data),
//            (data, exp) -> new PerfectHashTableExperiment(data),
//            (data, exp) -> new HashTableBitmapExperiment(data),
//            (data, exp) -> new vEBBitmapMinTreeExperiment(data)
//            DynamicHashTableBitmapExperiment::new,
//            ABHashTableExperiment::new,
//            ABHashTableRBTExperiment::new,
//            OpenAddresssingHashTableExperiment::new
            ChainedDivisionHashTableExperiment::new
        );

        try {
            for (int exponent = LOWEST_EXPONENT; exponent <= HIGHEST_EXPONENT; exponent++) {
                System.out.printf("Exponent: %d\n", exponent);
                for (int fileIndex = 0; fileIndex < N_FILES; fileIndex++) {
                    System.out.printf("  FileIndex: %d\n", fileIndex);
                    ExperimentData data = DataReader.read(String.format(
                            "data-generation/normal/n-2^%d-%d.data", exponent, fileIndex));
                    for (BiFunction<ExperimentData, Integer, Experiment> creator: experimentCreators) {
                        Experiment e = creator.apply(data, exponent + 3);
                        System.out.println(e.getOutputName());
                        e.runTime(N_EXPERIMENTS);
                        e.writeTimesToFile(baseOutputFolder, exponent, fileIndex);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
