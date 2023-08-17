package util;

import weka.core.Instances;

public abstract class InstancesA {
    protected Instances training;
    protected Instances testing;

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
}
