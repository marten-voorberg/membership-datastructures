package experiment;

import datastructures.ABHashTable;
import datastructures.Membership;
import read.ExperimentData;

public class ABHashTableExperiment extends Experiment {
    private final int exponent;

    public ABHashTableExperiment(ExperimentData data, int exponent) {
        super(data, new String[]{"ChainedHashTable$Node", "ABHashTable"});
        this.exponent = exponent;
    }

    @Override
    public String getOutputName() {
        return "ABHashTable";
    }

    @Override
    protected Membership getNewInstance() {
        return new ABHashTable(1 << exponent);
//        return new ABHashTable(1198372);
    }
}
