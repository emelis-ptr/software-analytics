package milestone_two.balancing;

import util.MyLogger;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.supervised.instance.SMOTE;

import static enums.Balance.SMOTE;

public class Smote extends Balancing {

    public Smote(Instances training, Instances testing) {
        super(training, testing);
        this.nameBalancing = SMOTE;
        smote();
    }

    /**
     * SMOTE
     *
     */
    public void smote() {
        this.filterClassifier = new FilteredClassifier();
        try {
            SMOTE smote = new SMOTE();
            smote.setInputFormat(this.getTraining());
            this.filterClassifier.setFilter(smote);
        } catch (Exception e) {
            MyLogger.errorLog("Errore SMOTE");
            System.exit(1);
        }
    }
}
