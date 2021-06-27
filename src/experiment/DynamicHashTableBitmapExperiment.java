package experiment;

import datastructures.DynamicHashTableBitmap;
import datastructures.HashTableBitmap;
import datastructures.Membership;
import read.ExperimentData;

public class DynamicHashTableBitmapExperiment extends Experiment {
    private int exponent;

    public DynamicHashTableBitmapExperiment(ExperimentData data, int exponent) {
        super(data, new String[]{});
        this.exponent = exponent;
    }

    @Override
    public String getOutputName() {
        return "DynamicHashTableBitmap";
    }

    @Override
    protected Membership getNewInstance() {
        return new DynamicHashTableBitmap(exponent);
    }
}
