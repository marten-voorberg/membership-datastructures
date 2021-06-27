package experiment;

import datastructures.ABHashTableRBT;
import datastructures.Membership;
import read.ExperimentData;

public class ABHashTableRBTExperiment extends Experiment {
    private int exponent;

    public ABHashTableRBTExperiment(ExperimentData data, int exponent) {
        super(data, new String[]{});
        this.exponent = exponent;
    }

    @Override
    public String getOutputName() {
        return "ABHashTableRBT";
    }

    @Override
    protected Membership getNewInstance() {
        return new ABHashTableRBT(1 << exponent);
//        return new ABHashTableRBT(1198372);
    }
}
