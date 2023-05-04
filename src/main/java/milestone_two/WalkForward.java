package milestone_two;

import entity.Result;
import enums.Balancing;
import enums.Classifier;
import enums.CostSensitive;
import enums.FeatureSelection;
import milestone_one.MilestoneOne;
import milestone_two.balancing.Oversampling;
import milestone_two.balancing.Smote;
import milestone_two.balancing.Undersampling;
import milestone_two.feature_selection.BestFirst;
import util.Logger;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.supervised.instance.SpreadSubsample;

import java.util.ArrayList;

import static milestone_two.MilestoneTwo.PROJ_NAME_M2;

public class WalkForward {

    private WalkForward() {
    }

    protected static final ArrayList<Result> results = new ArrayList<>();

    /**
     * Walk Forward
     *
     * @param instances:    instanze del training e testing set
     * @param indexRelease: indice della release
     */
    public static void runWalkFarward(Instances[] instances, int indexRelease) {
        Instances trainingSet = instances[0];
        Instances testingSet = instances[1];

        //Scelta del classificatore
        AbstractClassifier classifier = null;

        String[] models = new String[4];
        for (Classifier classifierName : Classifier.values()) {
            models[0] = String.valueOf(classifierName);

            switch (classifierName) {
                case RANDOM_FOREST -> //Random Forest
                        classifier = new RandomForest();
                case IBK -> //IBk
                        classifier = new IBk();
                case NAIVE_BAYES -> //Naive Bayes
                        classifier = new NaiveBayes();
                default -> {
                    Logger.errorLog("Errore nella selezione del classifier");
                    System.exit(1);
                }
            }
            chooseFeatureSelection(models, classifier, trainingSet, testingSet, indexRelease);
        }
    }

    /**
     * Feature Selection
     *
     * @param classifier:   AstractClassifier
     * @param trainingSet:  training set
     * @param testingSet:   testing set
     * @param indexRelease: indice della release
     */
    private static void chooseFeatureSelection(String[] models, AbstractClassifier classifier, Instances trainingSet, Instances testingSet, int indexRelease) {

        for (FeatureSelection featureSelectionName : FeatureSelection.values()) {
            models[1] = String.valueOf(featureSelectionName);

            Filter filter;
            switch (featureSelectionName) {
                case NO_SELECTION -> { //No selection
                    //setta la feature da predire
                    trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
                    testingSet.setClassIndex(trainingSet.numAttributes() - 1);
                }
                case BEST_FIRST -> { //Best First
                    filter = BestFirst.fsWithBestFirst(trainingSet);
                    try {
                        trainingSet = Filter.useFilter(trainingSet, filter);
                        testingSet = Filter.useFilter(testingSet, filter);

                        trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
                        testingSet.setClassIndex(trainingSet.numAttributes() - 1);

                    } catch (Exception e1) {
                        Logger.errorLog("Errore nella feature selection BestFirst");
                    }
                }
                default -> {
                    Logger.errorLog("Errore nella selezione della feature selection");
                    System.exit(1);
                }
            }
            chooseBalancing(models, classifier, trainingSet, testingSet, indexRelease);
        }
    }

    /**
     * Balancing
     *
     * @param classifier:   AbstractClassifier
     * @param trainingSet:  training set
     * @param testingSet:   testing set
     * @param indexRelease: numero delle release
     */
    private static void chooseBalancing(String[] models, AbstractClassifier classifier, Instances trainingSet, Instances testingSet, int indexRelease) {

        for (Balancing resamplingMethodName : Balancing.values()) {
            models[2] = String.valueOf(resamplingMethodName);

            FilteredClassifier filteredClassifier = null;
            switch (resamplingMethodName) {
                case NO_RESAMPLING: //No resampling
                    break;

                case OVERSAMPLING: //Oversampling
                    Resample resample = Oversampling.oversampling(trainingSet);

                    filteredClassifier = new FilteredClassifier();
                    filteredClassifier.setClassifier(classifier);
                    filteredClassifier.setFilter(resample);

                    try {
                        trainingSet = Filter.useFilter(trainingSet, resample);
                    } catch (Exception e1) {
                        Logger.errorLog("Errore nell'applicazione di Oversampling");
                    }
                    break;

                case UNDERSAMPLING: //Undersampling
                    SpreadSubsample spreadSubsample = Undersampling.undersampling(trainingSet);

                    filteredClassifier = new FilteredClassifier();
                    filteredClassifier.setClassifier(classifier);
                    filteredClassifier.setFilter(spreadSubsample);

                    try {
                        trainingSet = Filter.useFilter(trainingSet, spreadSubsample);
                    } catch (Exception e1) {
                        Logger.errorLog("Errore nell'applicazione di Undersampling");
                    }
                    break;

                case SMOTE: //SMOTE
                    SMOTE smote = Smote.smote(trainingSet);

                    filteredClassifier = new FilteredClassifier();
                    filteredClassifier.setClassifier(classifier);
                    filteredClassifier.setFilter(smote);

                    try {
                        trainingSet = Filter.useFilter(trainingSet, smote);
                    } catch (Exception e1) {
                        Logger.errorLog("Errore nell'applicazione di SMOTE");
                    }
                    break;

                default: //No sampling
                    break;
            }
            chooseSensitive(models, classifier, filteredClassifier, trainingSet, testingSet, indexRelease);
        }
    }

    /**
     * CostSensitive
     *
     * @param classifier:         AbstractClassifier
     * @param filteredClassifier: FilteredClassifier
     * @param trainingSet:        training set
     * @param testingSet:         testing set
     * @param indexRelease:       indice della release
     */
    private static void chooseSensitive(String[] models, AbstractClassifier classifier, FilteredClassifier filteredClassifier, Instances trainingSet, Instances testingSet, int indexRelease) {

        for (CostSensitive costSensitive : CostSensitive.values()) {
            models[3] = costSensitive.toString();

            CostSensitiveClassifier costSensitiveClassifier = null;
            switch (costSensitive) {
                case NO_SENSITIVE: //No sensitive
                    break;

                case SENSITIVE_THRESHOLD:
                    costSensitiveClassifier = new CostSensitiveClassifier();

                    if (filteredClassifier == null) {
                        costSensitiveClassifier.setClassifier(classifier);
                    } else {
                        costSensitiveClassifier.setClassifier(filteredClassifier);
                    }
                    costSensitiveClassifier.setMinimizeExpectedCost(true);
                    costSensitiveClassifier.setCostMatrix(milestone_two.cost_sensitive.CostSensitive.createCostMatrix(1.0, 10.0));

                    break;

                case SENSITIVE_LEARNING:
                    costSensitiveClassifier = new CostSensitiveClassifier();

                    if (filteredClassifier == null) {
                        costSensitiveClassifier.setClassifier(classifier);
                    } else {
                        costSensitiveClassifier.setClassifier(filteredClassifier);
                    }
                    costSensitiveClassifier.setMinimizeExpectedCost(false);
                    costSensitiveClassifier.setCostMatrix(milestone_two.cost_sensitive.CostSensitive.createCostMatrix(1.0, 10.0));

                    break;

                default:
                    break;
            }
            evaluation(models, classifier, filteredClassifier, costSensitiveClassifier, trainingSet, testingSet, indexRelease);
        }
    }

    /**
     * Applica i classificatori e calcola i valori
     *
     * @param classifier:  AbstractClassifier
     * @param trainingSet: training set
     * @param testingSet:  testing set
     */
    private static void evaluation(String[] models, AbstractClassifier classifier, FilteredClassifier filteredClassifier, CostSensitiveClassifier costSensitiveClassifier, Instances trainingSet, Instances testingSet, int indexRelease) {
        Result result = new Result(models[0], models[1], models[2], models[3]);
        result.setProjName(MilestoneOne.project(PROJ_NAME_M2));

        try {
            Evaluation eval = new Evaluation(testingSet);
            if (costSensitiveClassifier == null) {
                if (filteredClassifier == null) {
                    classifier.buildClassifier(trainingSet);
                    eval.evaluateModel(classifier, testingSet);
                } else {
                    filteredClassifier.buildClassifier(trainingSet);
                    eval.evaluateModel(filteredClassifier, testingSet);
                }
            } else {
                costSensitiveClassifier.buildClassifier(trainingSet);
                eval.evaluateModel(costSensitiveClassifier, testingSet);
            }
            result.addValues(eval);

            Logger.infoLog(models[0] + " - " + models[1] + " - " + models[2] + " - " + models[3] + "\n" +
                    "Error rate = " + eval.errorRate() + "\n" +
                    "Root mean squared error = " + eval.rootMeanSquaredError() + "\n");
        } catch (
                Exception e) {
            Logger.errorLog("Error evaluation");
        }
        result.addPercentageBuggyness(trainingSet, testingSet, indexRelease);
        results.add(result);
    }

}
