package milestone_two;

import util.Utils;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static util.Constants.*;

public class SplitDataset {
    private SplitDataset() {
    }

    /**
     * Crea il file per il training
     *
     * @param releaseIndex:
     * @throws IOException:
     * @return:
     */
    public static Instances[] setTestingTraining(int releaseIndex) throws Exception {
        ConverterUtils.DataSource sourceTraining = new ConverterUtils.DataSource(PATH_RESULTS_ARFF + MilestoneTwo.PROJ_NAME + DATASET_ARFF);
        Instances data = sourceTraining.getDataSet();

        Instances[] trainTest = new Instances[2];

        Instances trainingSet = new Instances(data, 0);
        Instances testSet = new Instances(data, 0);
        //per ogni istanza, se la release è precedente a quella da usare come test set allora
        //si aggiunge quell'istanza al training set, altrimenti, se e' uguale, la aggiungo al test set
        int index = data.attribute("Release").index();
        for (Instance i : data) {
            if ((int) i.value(index) < releaseIndex) {
                trainingSet.add(i);
            } else if ((int) i.value(index) == releaseIndex) {
                testSet.add(i);
            }
        }
        trainTest[0] = trainingSet;
        trainTest[1] = testSet;
        return trainTest;
    }

    /**
     * Trova il numero totale delle release
     *
     * @return :
     * @throws IOException :
     */
    public static Integer findTotalReleasesNumber() throws IOException {
        Utils.convertToArff();
        String datasetPath = PATH_RESULTS_M2 + MilestoneTwo.PROJ_NAME + DATASET_CSV;

        String line;
        int totalReleases = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath))) {

            // Saltiamo la prima riga del dataset che contiene i nomi delle colonne
            readLine(br);
            while ((line = readLine(br)) != null) {

                String[] fields = line.split(SPLIT_VIRGOLA);
                int releaseNumber;
                releaseNumber = Integer.parseInt(fields[0]);

                // Contiamo quante sono le release totali
                if (releaseNumber != totalReleases) {
                    totalReleases++;
                }
            }
        }
        return totalReleases;
    }

    /**
     * Ritorna una stringa contenente il contenuto di una linea
     *
     * @param br: BufferedReader
     * @throws IOException:
     * @return: contenuto di una linea
     */
    private static String readLine(BufferedReader br) throws IOException {
        return br.readLine();
    }

}
