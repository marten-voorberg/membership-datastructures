package experiment;

import datastructures.Membership;
import read.ExperimentData;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

public abstract class Experiment {
    protected String[] classIdentifiers;
    protected final ExperimentData data;

    protected long[] setupTimes;
    protected long[] experimentTimes;

    public Experiment(ExperimentData data, String[] classIdentifiers) {
        this.data = data;
        this.classIdentifiers = classIdentifiers;
    }

    public abstract String getOutputName();

    protected abstract Membership getNewInstance();

    public void runTime(int iterationAmount) {
        this.experimentTimes = new long[iterationAmount];
        this.setupTimes = new long[iterationAmount];

        for (int iter = 0; iter < iterationAmount; iter++) {
//            System.out.printf("    iteration: %d\n", iter);
            // Measure setup time
            long startSetup = System.currentTimeMillis();
            Membership ds = getNewInstance();

            for (int i = 0; i < data.SETUP_NUMBERS.length; i++) {
                ds.insert(data.SETUP_NUMBERS[i]);
            }

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

//            System.gc();
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public int runMemory() {
        int result = -1;
        Membership ds = getNewInstance();
        for (int i = 0; i < data.SETUP_NUMBERS.length; i++) {
            ds.insert(data.SETUP_NUMBERS[i]);
        }

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

    public void writeTimesToFile(String folderPath, int exponent, int fileIndex) throws IOException {
        final String filePath = String.format("%s/%s/t-%d-%d", folderPath, getOutputName(), exponent, fileIndex);
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
        writeArray(writer, setupTimes);
        writeArray(writer, experimentTimes);
    }

    private void writeArray(BufferedWriter writer, long[] setupTimes) throws IOException {
        for (int i = 0; i < setupTimes.length; i++) {
            writer.write(Long.toString(setupTimes[i]));
            if (i == setupTimes.length - 1) {
                writer.write("\n");
            } else {
                writer.write(",");
            }
        }
        writer.flush();
    }



    public long[] getSetupTimes() {
        return setupTimes;
    }

    public long[] getExperimentTimes() {
        return experimentTimes;
    }

//    public static void main(String[] args) {
//        try {
//            Experiment e = new Experiment(DataReader.read("data-generation/u-256-20256.data"),
//                10);
//            e.run();
//            e.writeToFile("output/splaytree");
////            System.out.println("Setup times:");
////            long[] setupTimes = e.getSetupTimes();
////            for (int i = 0; i < setupTimes.length; i++) {
////                System.out.println(setupTimes[i]);
////            }
////
////            System.out.println("Experiment times:");
////            long[] expTimes = e.getExperimentTimes();
////            DoubleSummaryStatistics experimentStats = Arrays.stream(expTimes)
////                .mapToDouble(x -> x)
////                .summaryStatistics();
////            System.out.println(experimentStats);
////            for (int i = 0; i < expTimes.length; i++) {
////                System.out.println(expTimes[i]);
////            }
//
//        } catch (MalformedDataException | IOException e) {
//            e.printStackTrace();
//        }
//    }
}
