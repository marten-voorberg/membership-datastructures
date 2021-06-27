package experiment;

import datastructures.Membership;
import datastructures.OpenAddressingHashTable;
import read.ExperimentData;

public class OpenAddresssingHashTableExperiment extends Experiment {
    private int exponent;

    public OpenAddresssingHashTableExperiment(ExperimentData data, int exponent) {
        super(data, new String[]{});
        this.exponent = exponent;
    }

    @Override
    public String getOutputName() {
        return "OpenAddressingHashTable";
    }

    @Override
    protected Membership getNewInstance() {
        return new OpenAddressingHashTable(1 << exponent);
//        return new OpenAddressingHashTable(1198372);
    }
}
