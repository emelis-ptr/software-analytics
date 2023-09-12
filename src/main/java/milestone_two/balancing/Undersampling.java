package milestone_two.balancing;

import util.MyLogger;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.supervised.instance.SpreadSubsample;

import static enums.Balance.UNDERSAMPLING;

public class Undersampling extends Balancing{

    public Undersampling(Instances training, Instances testing) {
        super(training, testing);
        this.nameBalancing = UNDERSAMPLING;
        undersampling();
    }

    /**
     * Undersampling
     *
     */
    public void undersampling() {
        this.filterClassifier = new FilteredClassifier();
        String[] opts = new String[]{"-M", "1.0"};
        try {
            SpreadSubsample spreadSubsample = new SpreadSubsample();
            spreadSubsample.setInputFormat(this.getTraining());
            spreadSubsample.setOptions(opts);
            this.filterClassifier.setFilter(spreadSubsample);
        } catch (Exception e) {
            MyLogger.errorLog("Errore Undersampling");
            System.exit(1);
        }
    }

}
