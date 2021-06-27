package experiment;

import datastructures.Membership;
import datastructures.vEBBitmapMinTree;
import datastructures.vEBBitmapTree;
import read.ExperimentData;

public class vEBBitmapMinTreeExperiment extends Experiment {
    public vEBBitmapMinTreeExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.vEBBitmapMinTree"});
    }

    @Override
    public String getOutputName() {
        return "vEBBitmapMin";
    }

    @Override
    protected Membership getNewInstance() {
        return new vEBBitmapMinTree(32);
    }
}
