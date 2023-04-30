package milestone_two;

import entity.Result;
import enums.Balancing;
import enums.Classifier;
import enums.FeatureSelection;
import enums.Project;
import milestone_one.MilestoneOne;
import util.Logger;
import util.WriteCSV;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MilestoneTwo {
    public static final String PROJ_NAME2 = String.valueOf(Project.BOOKKEEPER);

    public static void main(String[] args) throws IOException {
        Logger.setupLogger();
        Logger.infoLog(" --> " + MilestoneOne.project(PROJ_NAME2));

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
            for (Classifier classifierName : Classifier.values()) {
                for (Balancing resamplingMethodName : Balancing.values()) {
                    for (FeatureSelection featureSelectionName : FeatureSelection.values()) {
                        Logger.infoLog(" --> Consideriamo: " + classifierName + ", " + resamplingMethodName + ", "
                                + featureSelectionName);
                        Result result = new Result(classifierName, featureSelectionName, resamplingMethodName);
                        result.setProjName(MilestoneOne.project(PROJ_NAME2));
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


