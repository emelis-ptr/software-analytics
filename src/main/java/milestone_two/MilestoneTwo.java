package milestone_two;

import enums.Project;
import milestone_one.MilestoneOne;
import util.Logger;
import util.WriteCSV;
import weka.core.Instances;

import java.io.IOException;

public class MilestoneTwo {
    public static final String PROJ_NAME_M2 = String.valueOf(Project.BOOKKEEPER);

    public static void main(String[] args) throws IOException {
        Logger.setupLogger();
        Logger.infoLog(" --> " + MilestoneOne.project(PROJ_NAME_M2));

        int totalReleases = TrainingAndTestingSet.findTotalReleasesNumber();
        Logger.infoLog(" --> Numero di release: " + totalReleases);

        Logger.infoLog(" --> Inizio valutazione metriche per la classificazione");
        evaluate(totalReleases);

        Logger.infoLog(" --> Stampiamo su un file csv");
        WriteCSV.writeEvaluation(WalkForward.results);
    }

    /**
     * Calcola le metriche per ogni classifier, resampling, feature selection
     *
     * @param totalReleases: numero totale delle release
     */
    private static void evaluate(int totalReleases) {
        for (int i = 2; i < totalReleases + 1; i++) {
            Logger.infoLog("Current release: " + i + "/" + totalReleases);
            try {
                Instances[] instances = TrainingAndTestingSet.getTestingTraining(i);
                WalkForward.runWalkFarward(instances, i);
            } catch (Exception e) {
                Logger.errorLog("Exception training and testing set");
            }
        }
    }
}


