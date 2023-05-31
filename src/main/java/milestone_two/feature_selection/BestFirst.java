package milestone_two.feature_selection;

import util.Logger;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import static enums.FS.BEST_FIRST;

public class BestFirst extends FeatureSelection {

    public BestFirst(Instances trainingSet, Instances testingSet) {
        super(trainingSet, testingSet);
        this.nameFeatureSelection = BEST_FIRST;
        fsWithBestFirst();
    }

    public void fsWithBestFirst() {
        AttributeSelection featureSelection = new AttributeSelection();
        Instances trainingFiltered;
        Instances testingFiltered;

        try {

            CfsSubsetEval cfsSubsetEval = new CfsSubsetEval();
            //si imposta l'algoritmo per la ricerca backward
            GreedyStepwise greedyStepwise = new GreedyStepwise();
            //set the algorithm to search backward
            greedyStepwise.setSearchBackwards(true);
            //set the filter to use the evaluator and search algorithm
            featureSelection.setEvaluator(cfsSubsetEval);
            featureSelection.setSearch(greedyStepwise);

            //si specifica il dataset da usare
            featureSelection.setInputFormat(training);

            trainingFiltered = Filter.useFilter(training, featureSelection);
            testingFiltered = Filter.useFilter(testing, featureSelection);

            int numAttrFiltered = trainingFiltered.numAttributes();
            trainingFiltered.setClassIndex(numAttrFiltered - 1);
            testingFiltered.setClassIndex(numAttrFiltered - 1);

            this.training = trainingFiltered;
            this.testing = testingFiltered;
        } catch (Exception e) {
            Logger.errorLog("Errore nella feature selection con Best First");
        }
    }

}
