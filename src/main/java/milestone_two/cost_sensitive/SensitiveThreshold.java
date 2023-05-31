package milestone_two.cost_sensitive;

import util.Logger;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;

import static enums.Sensitive.SENSITIVE_THRESHOLD;

public class SensitiveThreshold extends Sensitivity {

    public SensitiveThreshold(FilteredClassifier filteredClassifier, Instances training) {
        super(filteredClassifier);
        this.sensitivityName = SENSITIVE_THRESHOLD;
        sensitiveThreshold(training);
    }

    public void sensitiveThreshold(Instances training){
        costSensitiveClassifier.setCostMatrix(createCostMatrix(1.0, 1.0));
        costSensitiveClassifier.setMinimizeExpectedCost(true);
        costSensitiveClassifier.setClassifier(filteredClassifier);
        try {
            costSensitiveClassifier.buildClassifier(training);
        } catch (Exception e) {
            Logger.infoLog("ErroreSensitive Threshold");
        }
    }
}
