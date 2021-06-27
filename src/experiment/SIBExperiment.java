package experiment;

import datastructures.Membership;
import datastructures.SuperImposedBitmap;
import read.ExperimentData;

public class SIBExperiment extends Experiment {
    public SIBExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.SIBNode"});
    }

    @Override
    public String getOutputName() {
        return "SuperImposedBitmap";
    }

    @Override
    protected Membership getNewInstance() {
        return new SuperImposedBitmap();
    }
}
