package entity;

import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class Result {
    private String projName;
    private int numTrainingRelease;
    private String classifierName;
    private String resamplingMethodName;
    private String featureSelectionName;
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

    public Result(String classifierName, String featureSelectionName, String resamplingMethodName) {
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

    public String getClassifierName() {
        return classifierName;
    }

    public void setClassifierName(String classifierName) {
        this.classifierName = classifierName;
    }

    public String getResamplingMethodName() {
        return resamplingMethodName;
    }

    public void setResamplingMethodName(String resamplingMethodName) {
        this.resamplingMethodName = resamplingMethodName;
    }

    public String getFeatureSelectionName() {
        return featureSelectionName;
    }

    public void setFeatureSelectionName(String featureSelectionName) {
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
    public void addPercentage(Instances training, Instances testing, int releaseIndex) {
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
     * @param trainingSet: training set
     */
    public void addValues(Evaluation eval, Instances trainingSet) {
        int positiveResultIndex = getNumInstancesTrue(trainingSet);

        this.tp = eval.numTruePositives(positiveResultIndex);
        this.fp = eval.numFalsePositives(positiveResultIndex);
        this.tn = eval.numTrueNegatives(positiveResultIndex);
        this.fn = eval.numFalseNegatives(positiveResultIndex);

        if (tp == 0 && fp == 0 && fn == 0) {
            this.precision = 1;
            this.recall = 1;
        } else if (tp == 0 && (fp > 0 || fn > 0)) {
            this.precision = 0;
            this.recall = 0;
        } else if (fp == 0 && tn == 0) {
            this.auc = 0;
        } else {
            this.precision = eval.precision(positiveResultIndex);
            this.recall = eval.recall(positiveResultIndex);
            this.auc = eval.areaUnderROC(1);
            this.kappa = eval.kappa();
        }
    }

    /**
     * Determina il numero di instanze true nel training set
     *
     * @param training: training set
     * @return:
     */
    public static int getNumInstancesTrue(Instances training) {
        int positiveValueIndexOfClassFeature = 0;
        int classFeatureIndex = 0;
        for (int i = 0; i < training.numAttributes(); i++) {
            if (training.attribute(i).name().equals(" Bugginess")) {
                classFeatureIndex = i;
                break;
            }
        }

        Attribute classFeature = training.attribute(classFeatureIndex);
        for (int i = 0; i < classFeature.numValues(); i++) {
            if (classFeature.value(i).equals("true")) {
                positiveValueIndexOfClassFeature += i;
            }
        }
        return positiveValueIndexOfClassFeature;
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
