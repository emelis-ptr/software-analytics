package milestone_two.balancing;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;

import static enums.Balance.NO_RESAMPLING;

public class NoBalancing extends Balancing {

    public NoBalancing(Instances trainset, Instances testset) {
        super(trainset, testset);
        this.nameBalancing = NO_RESAMPLING;
        noBalancing();
    }

    private void noBalancing(){
        this.filterClassifier = new FilteredClassifier();
    }
}
