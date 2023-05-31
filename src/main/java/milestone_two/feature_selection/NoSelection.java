package milestone_two.feature_selection;

import weka.core.Instances;

import static enums.FS.NO_SELECTION;

public class NoSelection extends FeatureSelection{

    public NoSelection(Instances training, Instances testing) {
        super(training, testing);
        this.nameFeatureSelection = NO_SELECTION;
        noSelection();
    }

    private void noSelection(){
        int numAttrNoFilter = training.numAttributes();
        training.setClassIndex(numAttrNoFilter - 1);
        testing.setClassIndex(numAttrNoFilter - 1);
    }
}
