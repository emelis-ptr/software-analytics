package milestone_two.balancing;

import enums.Balance;
import util.InstancesA;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;

public abstract class Balancing extends InstancesA {

    protected FilteredClassifier filterClassifier;
    protected Balance nameBalancing;

    protected Balancing(Instances training, Instances testing) {
        this.training = training;
        this.testing = testing;
    }

    public FilteredClassifier getFilterClassifier() {
        return filterClassifier;
    }

    public void setFilterClassifier(FilteredClassifier filterClassifier) {
        this.filterClassifier = filterClassifier;
    }

    public Balance getNameBalancing() {
        return nameBalancing;
    }

    public void setNameBalancing(Balance nameBalancing) {
        this.nameBalancing = nameBalancing;
    }
}
