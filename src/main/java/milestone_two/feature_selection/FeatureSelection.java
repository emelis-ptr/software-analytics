package milestone_two.feature_selection;

import enums.FS;
import weka.core.Instances;
import util.InstancesA;

public abstract class FeatureSelection extends InstancesA {
    protected FS nameFeatureSelection;

    protected FeatureSelection(Instances training, Instances testing) {
        this.training = training;
        this.testing = testing;
    }

    public FS getNameFeatureSelection() {
        return nameFeatureSelection;
    }

    public void setNameFeatureSelection(FS nameFeatureSelection) {
        this.nameFeatureSelection = nameFeatureSelection;
    }
}
