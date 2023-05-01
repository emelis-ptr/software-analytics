package milestone_two.balancing;

import util.Logger;
import weka.core.Instances;
import weka.filters.supervised.instance.Resample;

public class Oversampling {

    private Oversampling() {
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
}
