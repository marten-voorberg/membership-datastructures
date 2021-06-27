package experiment;

import datastructures.Membership;
import datastructures.vEBTree;
import read.ExperimentData;

public class vEBTreeExperiment extends Experiment {
    public vEBTreeExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.vEBTree"});
    }

    @Override
    public String getOutputName() {
        return "vEBTree";
    }

    @Override
    protected Membership getNewInstance() {
        return new vEBTree(32);
    }
}
