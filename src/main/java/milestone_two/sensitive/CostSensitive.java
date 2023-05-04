package milestone_two.sensitive;

import weka.classifiers.CostMatrix;

public class CostSensitive {

    private CostSensitive() {
    }

    /**
     * Matrice cost sensitive
     *
     * @param weightFalsePositive: peso per i falsi positivi
     * @param weightFalseNegative: per per i falsi negativi
     * @return: matrice di costo
     */
    public static CostMatrix createCostMatrix(double weightFalsePositive, double weightFalseNegative) {
        CostMatrix costMatrix = new CostMatrix(2);
        costMatrix.setCell(0, 0, 0.0);
        costMatrix.setCell(1, 0, weightFalsePositive);
        costMatrix.setCell(0, 1, weightFalseNegative);
        costMatrix.setCell(1, 1, 0.0);
        return costMatrix;
    }

}
