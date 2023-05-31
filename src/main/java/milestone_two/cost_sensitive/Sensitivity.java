package milestone_two.cost_sensitive;

import enums.Sensitive;
import weka.classifiers.CostMatrix;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.FilteredClassifier;

public class Sensitivity {

    protected CostMatrix costMatrix;
    protected CostSensitiveClassifier costSensitiveClassifier;
    protected FilteredClassifier filteredClassifier;
    protected Sensitive sensitivityName;

    public Sensitivity(FilteredClassifier filteredClassifier) {
        this.filteredClassifier = filteredClassifier;
        this.costSensitiveClassifier = new CostSensitiveClassifier();
    }

    /**
     * Matrice cost sensitive
     *
     * @param weightFalsePositive: peso per i falsi positivi
     * @param weightFalseNegative: per per i falsi negativi
     * @return: matrice di costo
     */
    public CostMatrix createCostMatrix(double weightFalsePositive, double weightFalseNegative) {
        costMatrix = new CostMatrix(2);
        costMatrix.setCell(0, 0, 0.0);
        costMatrix.setCell(0, 1, weightFalsePositive);
        costMatrix.setCell(1, 0, weightFalseNegative);
        costMatrix.setCell(1, 1, 0.0);
        return costMatrix;
    }

    public CostMatrix getCostMatrix() {
        return costMatrix;
    }

    public void setCostMatrix(CostMatrix costMatrix) {
        this.costMatrix = costMatrix;
    }

    public CostSensitiveClassifier getCostSensitiveClassifier() {
        return costSensitiveClassifier;
    }

    public void setCostSensitiveClassifier(CostSensitiveClassifier costSensitiveClassifier) {
        this.costSensitiveClassifier = costSensitiveClassifier;
    }

    public FilteredClassifier getFilteredClassifier() {
        return filteredClassifier;
    }

    public void setFilteredClassifier(FilteredClassifier filteredClassifier) {
        this.filteredClassifier = filteredClassifier;
    }

    public Sensitive getSensitivityName() {
        return sensitivityName;
    }

    public void setSensitivityName(Sensitive sensitivityName) {
        this.sensitivityName = sensitivityName;
    }
}
