package read;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.Stack;

public class DataReader {
    private static final String SETUP_MARKER = "SETUP";
    private static final String EXPERIMENT_MARKER = "EXPERIMENT";

    public static ExperimentData read(String filename) throws MalformedDataException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));


            String setupLine = reader.readLine();
            if (!setupLine.startsWith(SETUP_MARKER)) {
                throw new MalformedDataException(".data file should have a SETUP marker on " +
                    "the first line.");
            }

            final int SETUP_AMOUNT = Integer.parseInt(setupLine.split(" ")[1]);

            final int[] SETUP_NUMBERS = new int[SETUP_AMOUNT];
            for (int i = 0; i < SETUP_AMOUNT; i++) {
                SETUP_NUMBERS[i] = Integer.parseInt(reader.readLine());
            }

            String experimentLine = reader.readLine();
            if (!experimentLine.startsWith(EXPERIMENT_MARKER)) {
                throw new MalformedDataException(".data file should have a EXPERIMENT marker on " +
                    "the first line.");
            }

            final int EXPERIMENT_AMOUNT = Integer.parseInt(experimentLine.split(" ")[1]);
            final int[] EXPERIMENT_NUMBERS = new int[EXPERIMENT_AMOUNT];
            final int[] EXPERIMENT_OPERATIONS = new int[EXPERIMENT_AMOUNT];

            for (int i = 0; i < EXPERIMENT_AMOUNT; i++) {
                String[] split = reader.readLine().split(" ");
                EXPERIMENT_OPERATIONS[i] = Integer.parseInt(split[0]);
                EXPERIMENT_NUMBERS[i] = Integer.parseInt(split[1]);
            }

            return new ExperimentData(SETUP_NUMBERS, EXPERIMENT_OPERATIONS, EXPERIMENT_NUMBERS);
        } catch (IndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
            throw new MalformedDataException();
        }
    }

    public static void main(String[] args) {
        try {
            read("data-generation/u-5-5.data");
        } catch (MalformedDataException e) {
            e.printStackTrace();
        }
    }
}
