package experiment;

import datastructures.Membership;
import datastructures.RedBlackTree;
import read.ExperimentData;

public class RedBlackTreeExperiment extends Experiment {
    public RedBlackTreeExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.RedBlackTree$Node"});
    }

    @Override
    public String getOutputName() {
        return "RedBlackTree";
    }

    @Override
    protected Membership getNewInstance() {
        return new RedBlackTree();
    }
}
