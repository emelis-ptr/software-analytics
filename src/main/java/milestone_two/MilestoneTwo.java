package milestone_two;

import entity.Result;
import enums.Projects;
import util.MyLogger;
import util.WriteCSV;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.Utils.project;

public class MilestoneTwo {
    public static final String PROJ_NAME_M2 = project(String.valueOf(Projects.Project.BOOKKEEPER));

    public static void main(String[] args) throws IOException {
        MyLogger.setupLogger();
        MyLogger.infoLog(" --> " + PROJ_NAME_M2);

        int totalReleases = TrainingAndTestingSet.findTotalReleasesNumber();
        MyLogger.infoLog(" --> Numero di release: " + totalReleases);

        List<Result> resultList = new ArrayList<>();
        MyLogger.infoLog(" --> Inizio valutazione metriche per la classificazione");
        evaluate(resultList, totalReleases);

        MyLogger.infoLog(" --> Stampiamo su un file csv");
        WriteCSV.writeEvaluation(resultList);
    }

    /**
     * Calcola le metriche per ogni classifier, resampling, feature selection
     *
     * @param totalReleases: numero totale delle release
     */
    private static void evaluate(List<Result> resultList, int totalReleases) {
        for (int i = 2; i < totalReleases + 1; i++) {
            MyLogger.infoLog("Current release: " + i + "/" + totalReleases);
            try {
                Instances[] instances = TrainingAndTestingSet.getTestingTraining(i);
                WalkForward walkForward = new WalkForward(resultList, i);
                walkForward.runWalkFarward(instances);
            } catch (Exception e) {
                MyLogger.errorLog("Exception training and testing set");
            }
        }
    }
}


