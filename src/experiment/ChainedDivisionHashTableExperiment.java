package experiment;

import datastructures.ChainedDivisionHashTable;
import datastructures.Membership;
import read.ExperimentData;

public class ChainedDivisionHashTableExperiment extends Experiment {
    private int size;
    public ChainedDivisionHashTableExperiment(ExperimentData data, int size) {
        super(data, new String[]{"datastructures.ChainedHashTable$Node"});
        this.size = size;
    }

    @Override
    public String getOutputName() {
        return "ChainedDivisionHashTable";
    }

    @Override
    protected Membership getNewInstance() {
        return new ChainedDivisionHashTable(size);
    }
}
