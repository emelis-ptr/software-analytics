package milestone_two.balancing;

import enums.Balance;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;

public abstract class Balancing {

    protected Instances training;
    protected Instances testing;
    protected FilteredClassifier filterClassifier;
    protected Balance nameBalancing;

    protected Balancing(Instances training, Instances testing) {
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
