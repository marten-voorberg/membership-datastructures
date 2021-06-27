package experiment;

import datastructures.Membership;
import datastructures.vEBBitmapTree;
import read.ExperimentData;

public class vEBBitmapExperiment extends Experiment {
    public vEBBitmapExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.vEBBitmapTree"});
    }

    @Override
    public String getOutputName() {
        return "vEBBitmap";
    }

    @Override
    protected Membership getNewInstance() {
        return new vEBBitmapTree(32);
    }
}
