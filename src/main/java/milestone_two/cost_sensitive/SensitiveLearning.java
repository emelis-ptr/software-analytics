package milestone_two.cost_sensitive;

import util.MyLogger;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;

import static enums.Sensitive.SENSITIVE_LEARNING;

public class SensitiveLearning extends Sensitivity {

    public SensitiveLearning(FilteredClassifier filteredClassifier, Instances training) {
        super(filteredClassifier);
        this.sensitivityName = SENSITIVE_LEARNING;
        sensitiveLearning(training);
    }

    public void sensitiveLearning(Instances training) {
        costSensitiveClassifier.setCostMatrix(createCostMatrix(1.0, 10.0));
        costSensitiveClassifier.setMinimizeExpectedCost(false);
        costSensitiveClassifier.setClassifier(filteredClassifier);
        try {
            costSensitiveClassifier.buildClassifier(training);
        } catch (Exception e) {
            MyLogger.infoLog("Errore Sensitive Learning");
        }
    }
}
