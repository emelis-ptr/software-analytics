package milestone_two.feature_selection;

import enums.FS;
import weka.core.Instances;

public abstract class FeatureSelection {
    protected Instances training;
    protected Instances testing;
    protected FS nameFeatureSelection;

    protected FeatureSelection(Instances training, Instances testing) {
        this.training = training;
        this.testing = testing;
    }

    public Instances getTraining() {
        return training;
    }

    public void setTraining(Instances training) {
        this.training = training;
    }

    public Instances getTesting() {
        return testing;
    }

    public void setTesting(Instances testing) {
        this.testing = testing;
    }

    public FS getNameFeatureSelection() {
        return nameFeatureSelection;
    }

    public void setNameFeatureSelection(FS nameFeatureSelection) {
        this.nameFeatureSelection = nameFeatureSelection;
    }
}
