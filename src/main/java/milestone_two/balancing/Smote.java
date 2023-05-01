package milestone_two.balancing;

import util.Logger;
import weka.core.Instances;
import weka.filters.supervised.instance.SMOTE;

public class Smote {

    private Smote() {
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
