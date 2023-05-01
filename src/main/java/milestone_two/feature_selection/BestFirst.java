package milestone_two.feature_selection;

import util.Logger;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class BestFirst {

    private BestFirst() {
    }

    public static Filter fsWithBestFirst(Instances trainingSet) {
        AttributeSelection featureSelection = new AttributeSelection();
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
            featureSelection.setInputFormat(trainingSet);
        } catch (Exception e) {
            Logger.errorLog("Errore nella feature selection con Best First");
        }
        return featureSelection;
    }

}
