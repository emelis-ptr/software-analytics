package milestone_two;

import entity.Result;
import util.Logger;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.supervised.instance.SpreadSubsample;

public class WalkForward {

    private WalkForward() {
    }

    /**
     * Walk Forward
     *
     * @param result:       result
     * @param instances:    instanze del training e testing set
     * @param indexRelease: indice della release
     */
    public static void runWalkFarward(Result result, Instances[] instances, int indexRelease) {
        Instances trainingSet = instances[0];
        Instances testingSet = instances[1];

        //setta la feature da predire
        int numAttr = trainingSet.numAttributes();
        trainingSet.setClassIndex(numAttr - 1);
        testingSet.setClassIndex(numAttr - 1);

        //Scelta del classificatore
        AbstractClassifier classifier = null;

        switch (result.getClassifierName()) {
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
        chooseFeatureSelection(result, classifier, trainingSet, testingSet, indexRelease);
    }

    /**
     * Feature Selection
     *
     * @param result:       result
     * @param classifier:   AstractClassifier
     * @param trainingSet:  training set
     * @param testingSet:   testing set
     * @param indexRelease: indice della release
     */
    private static void chooseFeatureSelection(Result result, AbstractClassifier classifier, Instances trainingSet, Instances testingSet, int indexRelease) {
        Filter filter;

        Instances filteredTraining = null;
        Instances filteredTesting = null;

        switch (result.getFeatureSelectionName()) {
            case NO_SELECTION: //No selection
                break;

            case BEST_FIRST: //Best First
                filter = FeatureSelection.fsWithBestFirst(trainingSet);

                try {
                    filteredTraining = Filter.useFilter(trainingSet, filter);
                    filteredTesting = Filter.useFilter(testingSet, filter);

                    filteredTraining.setClassIndex(filteredTraining.numAttributes() - 1);
                    filteredTesting.setClassIndex(filteredTraining.numAttributes() - 1);

                } catch (Exception e1) {
                    Logger.errorLog("Errore nell'applicazione di Feature Selection");
                }
                break;

            default:
                Logger.errorLog("Errore nella selezione della feature selection");
                System.exit(1);
                break;
        }
        // dato che non viene applicata sempre la feature selection
        // assegniamo il training set e il testing set originale
        if (filteredTraining == null) filteredTraining = trainingSet;
        if (filteredTesting == null) filteredTesting = testingSet;

        chooseBalancing(result, classifier, filteredTraining, filteredTesting, indexRelease);
    }

    /**
     * Balancing
     *
     * @param result:       result
     * @param classifier:   AbstractClassifier
     * @param trainingSet:  training set
     * @param testingSet:   testing set
     * @param indexRelease: numero delle release
     */
    private static void chooseBalancing(Result result, AbstractClassifier classifier, Instances trainingSet, Instances testingSet, int indexRelease) {

        FilteredClassifier filteredClassifier = null;

        switch (result.getResamplingMethodName()) {

            case NO_RESAMPLING: //No resampling
                break;

            case OVERSAMPLING: //Oversampling
                Resample resample = Sampler.oversampling(trainingSet);

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

                SpreadSubsample spreadSubsample = Sampler.undersampling(trainingSet);

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
                SMOTE smote = Sampler.smote(trainingSet);

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

        result.addPercentage(trainingSet, testingSet, indexRelease);
        evaluate(result, classifier, filteredClassifier, trainingSet, testingSet);
    }

    /**
     * Applica i classificatori e calcola i valori
     *
     * @param result:      result
     * @param classifier:  AbstractClassifier
     * @param trainingSet: training set
     * @param testingSet:  testing set
     */
    public static void evaluate(Result result, AbstractClassifier classifier, FilteredClassifier filteredClassifier, Instances trainingSet, Instances testingSet) {
        try {
            Evaluation eval = new Evaluation(trainingSet);
            if (filteredClassifier == null) {
                classifier.buildClassifier(trainingSet);
                eval.evaluateModel(classifier, testingSet);
            } else {
                filteredClassifier.buildClassifier(trainingSet);
                eval.evaluateModel(filteredClassifier, testingSet);
            }
            result.addValues(eval, trainingSet);
        } catch (
                Exception e) {
            Logger.errorLog("Error evaluation");
        }

    }

}
