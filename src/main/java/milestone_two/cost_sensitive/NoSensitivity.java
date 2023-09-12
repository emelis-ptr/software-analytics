package milestone_two.cost_sensitive;

import util.MyLogger;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;

import static enums.Sensitive.NO_SENSITIVE;

public class NoSensitivity extends Sensitivity {

    public NoSensitivity(FilteredClassifier filteredClassifier, Instances training) {
        super(filteredClassifier);
        this.sensitivityName = NO_SENSITIVE;
        noSensitive(training);
    }

    private void noSensitive(Instances training) {
        costSensitiveClassifier.setMinimizeExpectedCost(false);
        costSensitiveClassifier.setClassifier(filteredClassifier);
        costSensitiveClassifier.setCostMatrix(createCostMatrix(1.0, 1.0));
        try {
            costSensitiveClassifier.buildClassifier(training);
        } catch (Exception e) {
            MyLogger.infoLog("Errore No Sensitive");
        }
    }
}
