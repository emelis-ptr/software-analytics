package util;

import milestone_two.MilestoneTwo;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.Constants.*;

public class Utils {
    private Utils() {
    }

    /**
     * Converte date in LocalDate
     *
     * @param dateToConvert:
     * @return:
     */
    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Metodo che verifica se una stringa è contenuta in un testo
     *
     * @param comment:
     * @param find:
     * @return:
     */
    public static boolean isContained(String comment, String find) {
        String pattern = "\\b" + find + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(comment);
        return m.find();
    }

    /**
     * Converte un file csv in un file arff
     *
     * @throws IOException :
     */
    public static void convertToArff() throws IOException {
        String child = PATH_RESULTS_M2 + MilestoneTwo.PROJ_NAME_M2 + DATASET_CSV;
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(ROOT, child));
        Instances data = loader.getDataSet();//get instances object
        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);//set the dataset we want to convert
        //and save as ARFF
        String saveFile = PATH_RESULTS_ARFF + MilestoneTwo.PROJ_NAME_M2 + DATASET_ARFF;
        saver.setFile(new File(ROOT, saveFile));
        saver.writeBatch();
    }

}
