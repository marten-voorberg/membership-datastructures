package read;

public class ExperimentData {
    public static final int INSERT = 0;
    public static final int IS_MEMBER = 1;
    public static final int DELETE = 2;

    public final int[] SETUP_NUMBERS;
    public final int[] EXPERIMENT_OPERATIONS;
    public final int[] EXPERIMENT_NUMBERS;

    public ExperimentData(int[] SETUP_NUMBERS, int[] EXPERIMENT_OPERATIONS,
                          int[] EXPERIMENT_NUMBERS) {
        this.SETUP_NUMBERS = SETUP_NUMBERS;
        this.EXPERIMENT_OPERATIONS = EXPERIMENT_OPERATIONS;
        this.EXPERIMENT_NUMBERS = EXPERIMENT_NUMBERS;
    }
}
