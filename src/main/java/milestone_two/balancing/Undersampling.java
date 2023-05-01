package milestone_two.balancing;

import util.Logger;
import weka.core.Instances;
import weka.filters.supervised.instance.SpreadSubsample;

public class Undersampling {

    private Undersampling() {
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

}
