package milestone_two.classification;

import enums.Classifier;
import util.MyLogger;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import static enums.Classifier.*;

public class Classification {

    private Instances training;
    private Instances testing;
    private FilteredClassifier filteredClassifier;
    private Classifier classifierName;

    public Classification(Instances training, Instances testing, FilteredClassifier filteredClassifier) {
        this.training = training;
        this.testing = testing;
        this.filteredClassifier = filteredClassifier;
    }

    /**
     * Clasificatore IBK
     */
    public void ibk() {
        try {
            // create and build new classifier
            IBk ibk = new IBk();
            filteredClassifier.setClassifier(ibk);
            filteredClassifier.buildClassifier(training);
            this.setClassifierName(IBK);
        } catch (Exception e) {
            MyLogger.errorLog("Errore" + IBK);
        }
    }

    /**
     * Clasificatore Naive Bayes
     */
    public void naiveBayesr() {
        try {
            NaiveBayes nb = new NaiveBayes();
            filteredClassifier.setClassifier(nb);
            filteredClassifier.buildClassifier(training);
            this.setClassifierName(NAIVE_BAYES);
        } catch (Exception e) {
            MyLogger.errorLog("Errore " + NAIVE_BAYES);
        }
    }

    /**
     * Clasificatore Random Forest
     */
    public void randomForest() {
        try {
            RandomForest rf = new RandomForest();
            filteredClassifier.setClassifier(rf);
            filteredClassifier.buildClassifier(training);
            this.setClassifierName(RANDOM_FOREST);
        } catch (Exception e) {
            MyLogger.errorLog("Errore " + RANDOM_FOREST);
        }
    }

    public Instances getTraining() {
        return training;
    }

    public void setTraining(Instances training) {
        this.training = training;
    }

    public Instances getTesting() {
        return testing;
    }

    public void setTesting(Instances testing) {
        this.testing = testing;
    }

    public FilteredClassifier getFilteredClassifier() {
        return filteredClassifier;
    }

    public void setFilteredClassifier(FilteredClassifier filteredClassifier) {
        this.filteredClassifier = filteredClassifier;
    }

    public Classifier getClassifierName() {
        return classifierName;
    }

    public void setClassifierName(Classifier classifierName) {
        this.classifierName = classifierName;
    }
}
