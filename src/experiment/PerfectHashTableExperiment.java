package experiment;

import datastructures.HashTableBitmap;
import datastructures.Membership;
import datastructures.PerfectHashTable;
import read.ExperimentData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PerfectHashTableExperiment extends Experiment {
    public PerfectHashTableExperiment(ExperimentData data) {
        super(data, new String[]{
            "java.lang.Integer",
            "[Ljava.lang.Integer",
            "[[Ljava.lang.Integer"
        });
    }

    @Override
    public String getOutputName() {
        return "PerfectHashTable";
    }

    @Override
    protected Membership getNewInstance() {
        throw new UnsupportedOperationException();
    }

    public void runTime(int iterationAmount) {
        this.experimentTimes = new long[iterationAmount];
        this.setupTimes = new long[iterationAmount];

        for (int iter = 0; iter < iterationAmount; iter++) {
//            System.out.printf("    iteration: %d\n", iter);
            // Measure setup time

            List<Integer> setupNumbers = new ArrayList<>();
            for (int i = 0; i < data.SETUP_NUMBERS.length; i++) {
                setupNumbers.add(data.SETUP_NUMBERS[i]);
            }

            long startSetup = System.currentTimeMillis();
            Membership htb = new PerfectHashTable(new HashSet<>(setupNumbers));

            long endSetup = System.currentTimeMillis();
            setupTimes[iter] = endSetup - startSetup;

            // Measure experiment time
            long startExperiment = System.currentTimeMillis();

            for (int i = 0; i < data.EXPERIMENT_OPERATIONS.length; i++) {
                int op = data.EXPERIMENT_OPERATIONS[i];
                int number = data.EXPERIMENT_NUMBERS[i];

                if (op == ExperimentData.INSERT) {
                    htb.insert(number);
                } else if (op == ExperimentData.IS_MEMBER) {
                    htb.isMember(number);
                } else if (op == ExperimentData.DELETE){
                    htb.delete(number);
                }
            }

            long endExperiment = System.currentTimeMillis();
            experimentTimes[iter] = endExperiment - startExperiment;
        }
    }

    @Override
    public int runMemory() {
        int result = -1;
        List<Integer> setupNumbers = new ArrayList<>();
        for (int i = 0; i < data.SETUP_NUMBERS.length; i++) {
            setupNumbers.add(data.SETUP_NUMBERS[i]);
        }
        PerfectHashTable htb = new PerfectHashTable(new HashSet<>(setupNumbers));

//        try {
//            String name = ManagementFactory.getRuntimeMXBean().getName();
//            String PID = name.substring(0, name.indexOf("@"));
//            Process p = Runtime.getRuntime().exec("jcmd " + PID + " GC.class_histogram");
//            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
////                input.lines().limit(10).forEach(System.out::println);
//                result = input.lines()
//                    .filter(line -> Arrays.stream(classIdentifiers).anyMatch(line::contains))
//                    .mapToInt(line -> Integer.parseInt(line.split(" +")[3]))
//                    .sum();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        result = htb.getByteSize();

        htb.isMember(10);

        if (result == -1) {
            throw new IllegalStateException("No memory information matching your class identifier was found.");
        }

        return result;
    }
}
