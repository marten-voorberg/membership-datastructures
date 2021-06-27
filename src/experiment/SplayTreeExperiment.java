package experiment;

import datastructures.Membership;
import datastructures.SplayTree;
import read.ExperimentData;

public class SplayTreeExperiment extends Experiment {
    public SplayTreeExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.SplayTree$Node"});
    }

    @Override
    public String getOutputName() {
        return "SplayTree";
    }

    @Override
    protected Membership getNewInstance() {
        return new SplayTree();
    }
}
