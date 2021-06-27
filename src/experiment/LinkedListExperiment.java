package experiment;

import datastructures.LinkedList;
import datastructures.Membership;
import read.ExperimentData;

public class LinkedListExperiment extends Experiment {
    public LinkedListExperiment(ExperimentData data) {
        super(data, new String[]{"datastructures.LinkedList$Node"});
    }

    @Override
    public String getOutputName() {
        return "LinkedList";
    }

    @Override
    protected Membership getNewInstance() {
        return new LinkedList();
    }
}
