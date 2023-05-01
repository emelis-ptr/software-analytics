package entity;

import enums.Balancing;
import enums.Classifier;
import enums.FeatureSelection;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

public class Result {
    private String projName;
    private int numTrainingRelease;
    private Classifier classifierName;
    private Balancing resamplingMethodName;
    private FeatureSelection featureSelectionName;
    private String costSensitivity;
    private double tp;
    private double fp;
    private double tn;
    private double fn;
    private double precision;
    private double recall;
    private double auc;
    private double kappa;
    private float percentageTraining;
    private float percentageBuggyInTraining;
    private float percentageBuggyInTesting;

    public Result(Classifier classifierName, FeatureSelection featureSelectionName, Balancing resamplingMethodName) {
        this.classifierName = classifierName;
        this.featureSelectionName = featureSelectionName;
        this.resamplingMethodName = resamplingMethodName;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public int getNumTrainingRelease() {
        return numTrainingRelease;
    }

    public void setNumTrainingRelease(int numTrainingRelease) {
        this.numTrainingRelease = numTrainingRelease;
    }

    public Classifier getClassifierName() {
        return classifierName;
    }

    public void setClassifierName(Classifier classifierName) {
        this.classifierName = classifierName;
    }

    public Balancing getResamplingMethodName() {
        return resamplingMethodName;
    }

    public void setResamplingMethodName(Balancing resamplingMethodName) {
        this.resamplingMethodName = resamplingMethodName;
    }

    public FeatureSelection getFeatureSelectionName() {
        return featureSelectionName;
    }

    public void setFeatureSelectionName(FeatureSelection featureSelectionName) {
        this.featureSelectionName = featureSelectionName;
    }

    public String getCostSensitivity() {
        return costSensitivity;
    }

    public void setCostSensitivity(String costSensitivity) {
        this.costSensitivity = costSensitivity;
    }

    public double getTp() {
        return tp;
    }

    public void setTp(double tp) {
        this.tp = tp;
    }

    public double getFp() {
        return fp;
    }

    public void setFp(double fp) {
        this.fp = fp;
    }

    public double getTn() {
        return tn;
    }

    public void setTn(double tn) {
        this.tn = tn;
    }

    public double getFn() {
        return fn;
    }

    public void setFn(double fn) {
        this.fn = fn;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getAuc() {
        return auc;
    }

    public void setAuc(double auc) {
        this.auc = auc;
    }

    public double getKappa() {
        return kappa;
    }

    public void setKappa(double kappa) {
        this.kappa = kappa;
    }

    public float getPercentageTraining() {
        return percentageTraining;
    }

    public void setPercentageTraining(float percentageTraining) {
        this.percentageTraining = percentageTraining;
    }

    public float getPercentageBuggyInTraining() {
        return percentageBuggyInTraining;
    }

    public void setPercentageBuggyInTraining(float percentageBuggyInTraining) {
        this.percentageBuggyInTraining = percentageBuggyInTraining;
    }

    public float getPercentageBuggyInTesting() {
        return percentageBuggyInTesting;
    }

    public void setPercentageBuggyInTesting(float percentageBuggyInTesting) {
        this.percentageBuggyInTesting = percentageBuggyInTesting;
    }

    /**
     * Si aggiungono la percentuale di bugginess nel training e nel testing set
     *
     * @param training:     training set
     * @param testing:      testing set
     * @param releaseIndex: numero della release
     */
    public void addPercentageBuggyness(Instances training, Instances testing, int releaseIndex) {
        this.numTrainingRelease = releaseIndex - 1;

        int numInstancesTraining = training.numInstances();
        int numInstancesTest = testing.numInstances();
        this.percentageTraining = ((float) numInstancesTraining / (numInstancesTraining + numInstancesTest)) * 100;

        int numBuggyTraining = 0;
        int numFeatures = training.numAttributes();
        for (Instance instance : training) {
            if ((instance.stringValue(numFeatures - 1)).equals("true")) {
                numBuggyTraining = numBuggyTraining + 1;
            }
        }

        int numBuggyTest = 0;
        for (Instance instance : testing) {
            if ((instance.stringValue(numFeatures - 1)).equals("true")) {
                numBuggyTest = numBuggyTest + 1;
            }
        }

        if (numBuggyTraining != 0) this.percentageBuggyInTraining = ((float) numBuggyTraining / training.size()) * 100;
        if (numBuggyTest != 0) this.percentageBuggyInTesting = ((float) numBuggyTest / testing.size()) * 100;
    }

    /**
     * Si aggiungono i valori di TP, FP, TN, FN, Precision, Recall, Auc e Kappa
     *
     * @param eval:        Evaluation
     */
    public void addValues(Evaluation eval) {
        int classIndex = 0;
        this.tp = eval.numTruePositives(classIndex);
        this.fp = eval.numFalsePositives(classIndex);
        this.tn = eval.numTrueNegatives(classIndex);
        this.fn = eval.numFalseNegatives(classIndex);

        if (tp == 0 && fp == 0 && fn == 0) {
            this.precision = 1;
            this.recall = 1;
        } else if (tp == 0 && (fp > 0 || fn > 0)) {
            this.precision = 0;
            this.recall = 0;
        } else if (fp == 0 && tn == 0) {
            this.auc = 0;
        } else {
            this.precision = eval.precision(classIndex);
            this.recall = eval.recall(classIndex);
            this.auc = eval.areaUnderROC(classIndex);
            this.kappa = eval.kappa();
        }
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "projName='" + projName + '\'' +
                ", numTrainingRelease=" + numTrainingRelease +
                ", classifierName='" + classifierName + '\'' +
                ", resamplingMethodName='" + resamplingMethodName + '\'' +
                ", featureSelectionName='" + featureSelectionName + '\'' +
                ", costSensitivity='" + costSensitivity + '\'' +
                ", tp=" + tp +
                ", fp=" + fp +
                ", tn=" + tn +
                ", fn=" + fn +
                ", precision=" + precision +
                ", recall=" + recall +
                ", auc=" + auc +
                ", kappa=" + kappa +
                ", percentageTraining=" + percentageTraining +
                ", percentageBuggyInTraining=" + percentageBuggyInTraining +
                ", percentageBuggyInTesting=" + percentageBuggyInTesting +
                '}';
    }
}
