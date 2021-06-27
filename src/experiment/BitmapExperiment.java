package experiment;

import datastructures.Bitmap;
import datastructures.Membership;
import read.ExperimentData;

public class BitmapExperiment extends Experiment {
    public BitmapExperiment(ExperimentData data) {
        super(data, new String[] {"I (java.base@14.0.1)", "[I"});
    }

    @Override
    public String getOutputName() {
        return "Bitmap";
    }

    @Override
    protected Membership getNewInstance() {
        return new Bitmap();
    }
}
