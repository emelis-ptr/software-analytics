package milestone_two;

import util.Logger;
import weka.core.Instances;
import weka.filters.supervised.instance.Resample;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.supervised.instance.SpreadSubsample;

public class Sampler {

    private Sampler() {
    }

    /**
     * Undersampling
     *
     * @param instances:
     * @return:
     */
    public static SpreadSubsample undersampling(Instances instances) {

        SpreadSubsample spreadSubsample = null;
        String[] opts = new String[]{"-M", "1.0"};
        try {
            spreadSubsample = new SpreadSubsample();
            spreadSubsample.setInputFormat(instances);
            spreadSubsample.setOptions(opts);
        } catch (Exception e) {
            Logger.errorLog("Errore Undersampling");
            System.exit(1);
        }

        return spreadSubsample;

    }

    /**
     * Oversampling
     *
     * @param training:
     * @return:
     */
    public static Resample oversampling(Instances training) {
        Resample resample = null;
        try {

            resample = new Resample();
            resample.setInputFormat(training);

        } catch (Exception e) {
            Logger.errorLog("Errore Oversampling");
            System.exit(1);
        }

        return resample;
    }

    /**
     * SMOTE
     *
     * @param training:
     * @return:
     */
    public static SMOTE smote(Instances training) {

        SMOTE smote = null;
        try {

            smote = new SMOTE();
            smote.setInputFormat(training);

        } catch (Exception e) {
            Logger.errorLog("Errore SMOTE");
            System.exit(1);
        }

        return smote;

    }
}
