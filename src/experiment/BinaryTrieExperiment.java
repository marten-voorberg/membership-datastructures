package experiment;

import datastructures.BinaryTrie;
import datastructures.Membership;
import read.ExperimentData;

public class BinaryTrieExperiment extends Experiment {
    public BinaryTrieExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.BinaryTrie$Node"});
    }

    @Override
    public String getOutputName() {
        return "BinaryTrie";
    }

    @Override
    protected Membership getNewInstance() {
        return new BinaryTrie();
    }
}
