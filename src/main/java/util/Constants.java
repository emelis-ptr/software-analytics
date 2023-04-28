package util;

import milestone_one.MilestoneOne;
import milestone_two.MilestoneTwo;

import java.io.File;

public class Constants {

    private Constants() {
    }

    public static final String PATH_RESULTS = "results/";
    public static final String PATH_REPOSITORY = "path_repo.properties";
    public static final String JAVA_EXT = ".java";
    public static final String PATH_RESULTS_M1 = PATH_RESULTS + MilestoneOne.PROJ_NAME + File.separator;
    public static final String PATH_RESULTS_M2 = PATH_RESULTS + MilestoneTwo.PROJ_NAME + File.separator;

    //Milestone one
    public static final String FIELDS = "fields";
    public static final String VERSIONS = "versions";
    public static final String CREATED = "created";
    public static final String RESOLUTION_DATE = "resolutiondate";
    public static final String RELEASE_DATE = "releaseDate";
    public static final String NAME_VERSION = "name";
    public static final String ID_VERSION = "id";

    public static final String ISSUES = "issues";
    public static final String TOTAL = "total";
    public static final String KEY = "key";

    // Milestone Two

    public static final String SPLIT_VIRGOLA = ",";
    public static final String DATASET_CSV = "-Dataset.csv";
    public static final String DATASET_ARFF = "-Dataset.arff";
    public static final String ROOT = System.getProperty("user.dir");
    public static final String PATH_RESULTS_ARFF = PATH_RESULTS + MilestoneTwo.PROJ_NAME + File.separator + "arff/";

}
