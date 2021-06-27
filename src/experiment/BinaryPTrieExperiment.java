package experiment;

import datastructures.BinaryPTrie;
import datastructures.BinaryTrie2;
import datastructures.Membership;
import read.ExperimentData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

public class BinaryPTrieExperiment extends Experiment {
    public BinaryPTrieExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.BinaryPTrie$Node"});
    }

    @Override
    public String getOutputName() {
        return "BinaryPTrie";
    }

    @Override
    protected Membership getNewInstance() {
        return null;
    }

    public void runTime(int iterationAmount) {
        this.experimentTimes = new long[iterationAmount];
        this.setupTimes = new long[iterationAmount];

        for (int iter = 0; iter < iterationAmount; iter++) {
//            System.out.printf("    iteration: %d\n", iter);
            // Measure setup time
            long startSetup = System.currentTimeMillis();
            BinaryTrie2 trie = new BinaryTrie2();

            for (int i = 0; i < data.SETUP_NUMBERS.length; i++) {
                trie.insert(data.SETUP_NUMBERS[i]);
            }

            Membership ds = new BinaryPTrie(trie);

            long endSetup = System.currentTimeMillis();
            setupTimes[iter] = endSetup - startSetup;

            // Measure experiment time
            long startExperiment = System.currentTimeMillis();

            for (int i = 0; i < data.EXPERIMENT_OPERATIONS.length; i++) {
                int op = data.EXPERIMENT_OPERATIONS[i];
                int number = data.EXPERIMENT_NUMBERS[i];

                if (op == ExperimentData.INSERT) {
                    ds.insert(number);
                } else if (op == ExperimentData.IS_MEMBER) {
                    ds.isMember(number);
                } else if (op == ExperimentData.DELETE){
                    ds.delete(number);
                }
            }

            long endExperiment = System.currentTimeMillis();
            experimentTimes[iter] = endExperiment - startExperiment;
        }
    }

    public int runMemory() {
        int result = -1;
        BinaryTrie2 trie = new BinaryTrie2();

        for (int i = 0; i < data.SETUP_NUMBERS.length; i++) {
            trie.insert(data.SETUP_NUMBERS[i]);
        }

        Membership ds = new BinaryPTrie(trie);

        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String PID = name.substring(0, name.indexOf("@"));
            Process p = Runtime.getRuntime().exec("jcmd " + PID + " GC.class_histogram");
            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
//                input.lines().limit(10).forEach(System.out::println);
                result = input.lines()
                    .filter(line -> Arrays.stream(classIdentifiers).anyMatch(line::contains))
                    .mapToInt(line -> Integer.parseInt(line.split(" +")[3]))
                    .sum();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // We use ds here so it does not get garbage collected before we have observed its memory usage.
        ds.isMember(10);

        if (result == -1) {
            throw new IllegalStateException("No memory information matching your class identifier was found.");
        }

        return result;
    }
}
