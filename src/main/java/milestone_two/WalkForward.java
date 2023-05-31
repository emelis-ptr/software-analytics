package milestone_two;

import entity.Result;
import enums.Balance;
import enums.Classifier;
import enums.FS;
import enums.Sensitive;
import milestone_one.MilestoneOne;
import milestone_two.balancing.*;
import milestone_two.classification.Classification;
import milestone_two.cost_sensitive.NoSensitivity;
import milestone_two.cost_sensitive.SensitiveLearning;
import milestone_two.cost_sensitive.SensitiveThreshold;
import milestone_two.cost_sensitive.Sensitivity;
import milestone_two.feature_selection.BestFirst;
import milestone_two.feature_selection.FeatureSelection;
import milestone_two.feature_selection.NoSelection;
import util.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;

import java.util.List;

import static milestone_two.MilestoneTwo.PROJ_NAME_M2;

public class WalkForward {

    private Balancing balancing;
    private Sensitivity sensitivity;
    private FeatureSelection featureSel;
    private Classification classification;
    private final List<Result> results;
    private final int indexRelease;

    public WalkForward(List<Result> results, int indexRelease) {
        this.results = results;
        this.indexRelease = indexRelease;
    }


    /**
     * Walk Forward
     *
     * @param instances: instanze del training e testing set
     */
    public void runWalkFarward(Instances[] instances) {
        Instances trainingSet = instances[0];
        Instances testingSet = instances[1];
        //Feature Selection
        chooseFeatureSelection(trainingSet, testingSet);
    }

    /**
     * Classifier
     */
    private void chooseClassifier() {
        Classification classifier = new Classification(featureSel.getTraining(), featureSel.getTesting(), balancing.getFilterClassifier());
        for (Classifier classifierName : Classifier.values()) {
            switch (classifierName) {
                case RANDOM_FOREST -> {//Random Forest
                    classifier.randomForest();
                    this.setClassification(classifier);
                }
                case IBK -> {//IBk
                    classifier.ibk();
                    this.setClassification(classifier);
                }
                case NAIVE_BAYES -> {//Naive Bayes
                    classifier.naiveBayesr();
                    this.setClassification(classifier);
                }
                default -> {
                    Logger.errorLog("Errore nella selezione del classifier");
                    System.exit(1);
                }
            }
            chooseSensitive(classifier);
        }
    }

    /**
     * Feature Selection
     *
     * @param trainingSet: training set
     * @param testingSet:  testing set
     */
    private void chooseFeatureSelection(Instances trainingSet, Instances testingSet) {

        for (FS FSName : FS.values()) {
            FeatureSelection featureSelection;
            switch (FSName) {
                case NO_SELECTION -> {//No selection
                    //feature da predire
                    featureSelection = new NoSelection(trainingSet, testingSet);
                    this.setFeatureSel(featureSelection);
                }
                case BEST_FIRST -> {//Best First
                    featureSelection = new BestFirst(trainingSet, testingSet);
                    this.setFeatureSel(featureSelection);
                }
                default -> {
                    Logger.errorLog("Errore nella selezione della feature selection");
                    System.exit(1);
                }
            }
            chooseBalancing();
        }
    }

    /**
     * Balancing
     */
    private void chooseBalancing() {

        Balancing balance;
        for (Balance resamplingMethodName : Balance.values()) {

            switch (resamplingMethodName) {
                case NO_RESAMPLING -> { //No resampling
                    balance = new NoBalancing(featureSel.getTraining(), featureSel.getTesting());
                    this.setBalancing(balance);
                }
                case OVERSAMPLING -> { //Oversampling
                    balance = new Oversampling(featureSel.getTraining(), featureSel.getTesting());
                    this.setBalancing(balance);
                }
                case UNDERSAMPLING -> { //Undersampling
                    balance = new Undersampling(featureSel.getTraining(), featureSel.getTesting());
                    this.setBalancing(balance);
                }
                case SMOTE -> { //SMOTE
                    balance = new Smote(featureSel.getTraining(), featureSel.getTesting());
                    this.setBalancing(balance);
                }
            }
            chooseClassifier();
        }
    }

    /**
     * CostSensitive
     *
     * @param classifier: classifier
     */
    private void chooseSensitive(Classification classifier) {

        for (Sensitive sensitive : Sensitive.values()) {
            Instances training = classifier.getTraining();
            FilteredClassifier filteredClassifier = classifier.getFilteredClassifier();
            Sensitivity sen;
            switch (sensitive) {
                case NO_SENSITIVE -> { //No sensitive
                    sen = new NoSensitivity(filteredClassifier, training);
                    this.setSensitivity(sen);
                }
                case SENSITIVE_THRESHOLD -> {
                    sen = new SensitiveThreshold(filteredClassifier, training);
                    this.setSensitivity(sen);
                }
                case SENSITIVE_LEARNING -> {
                    sen = new SensitiveLearning(filteredClassifier, training);
                    this.setSensitivity(sen);
                }
            }
            evaluation();
        }
    }

    /**
     * Applica i classificatori e calcola i valori
     */
    private void evaluation() {
        Result result = new Result(this.getClassification().getClassifierName().name(),
                this.getFeatureSel().getNameFeatureSelection().name(),
                this.getBalancing().getNameBalancing().name(),
                this.getSensitivity().getSensitivityName().name());
        result.setProjName(MilestoneOne.project(PROJ_NAME_M2));

        CostSensitiveClassifier costSensitiveClassifier = sensitivity.getCostSensitiveClassifier();
        Instances training = classification.getTraining();
        Instances testing = classification.getTesting();
        try {
            Evaluation eval = new Evaluation(testing, costSensitiveClassifier.getCostMatrix());
            eval.evaluateModel(costSensitiveClassifier, testing);
            result.addValues(eval);
        } catch (
                Exception e) {
            Logger.errorLog("Error evaluation");
        }
        result.addPercentageBuggyness(training, testing, indexRelease);
        results.add(result);
        Logger.infoLog("" + result);
    }

    public Balancing getBalancing() {
        return balancing;
    }

    public void setBalancing(Balancing balancing) {
        this.balancing = balancing;
    }

    public Sensitivity getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(Sensitivity sensitivity) {
        this.sensitivity = sensitivity;
    }

    public FeatureSelection getFeatureSel() {
        return featureSel;
    }

    public void setFeatureSel(FeatureSelection featureSel) {
        this.featureSel = featureSel;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public List<Result> getResults() {
        return results;
    }


}
