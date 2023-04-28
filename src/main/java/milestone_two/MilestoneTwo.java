package milestone_two;

import entity.Result;
import util.Logger;
import util.WriteCSV;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MilestoneTwo {
    protected static final String[] PROJS_NAME = {"Bookkeeper", "Syncope"};
    protected static final List<String> CLASSIFIER = new ArrayList<>(Arrays.asList("Random Forest", "IBk", "Naive Bayes"));
    protected static final List<String> RESAMPLING = new ArrayList<>(Arrays.asList("No resampling", "Oversampling", "Undersampling", "Smote"));
    protected static final List<String> FEATURE_SELECTION = new ArrayList<>(Arrays.asList("No Selection", "Best First"));
    protected static final List<String> COST_SENSITIVE = new ArrayList<>(Arrays.asList("No Sensitive", "Sensitive Threshold", "Sensitive Learning"));


    //bookkeeper = 0; syncope = 1
    public static final String PROJ_NAME = PROJS_NAME[0];

    public static void main(String[] args) throws IOException {
        Logger.setupLogger();
        Logger.infoLog(" --> " + PROJ_NAME);

        int totalReleases = SplitDataset.findTotalReleasesNumber();
        Logger.infoLog(" --> Numero di release: " + totalReleases);

        ArrayList<Result> results = new ArrayList<>();
        Logger.infoLog(" --> Inizio valutazione metriche per la classificazione");
        evaluate(totalReleases, results);

        Logger.infoLog(" --> Stampiamo su un file csv");
        WriteCSV.writeEvaluation(results);
    }

    /**
     * Calcola i valori per ogni classifier, resampling, feature selection
     *
     * @param totalReleases: numero totale delle release
     * @param results:       lista dei risultati finali
     */
    private static void evaluate(int totalReleases, List<Result> results) {
        for (int i = 2; i < totalReleases + 1; i++) {
            for (String classifierName : CLASSIFIER) {
                for (String resamplingMethodName : RESAMPLING) {
                    for (String featureSelectionName : FEATURE_SELECTION) {
                        Logger.infoLog(" --> Consideriamo: " + classifierName + ", " + resamplingMethodName + ", "
                                + featureSelectionName);
                        Result result = new Result(classifierName, featureSelectionName, resamplingMethodName);
                        result.setProjName(MilestoneTwo.PROJ_NAME);
                        run(result, i);
                        results.add(result);
                    }
                }
            }
        }
    }

    /**
     * Esegue la walk forward
     *
     * @param result: result
     * @param i:      indice della release
     */
    private static void run(Result result, int i) {
        try {
            Instances[] instances = SplitDataset.setTestingTraining(i);
            WalkForward.runWalkFarward(result, instances, i);
        } catch (Exception e) {
            Logger.errorLog("Exception Datasource");
        }
    }
}


