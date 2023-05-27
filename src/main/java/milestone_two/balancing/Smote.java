package milestone_two.balancing;

import milestone_two.TrainingAndTestingSet;
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

            double parameter = 0;
            // numero di instaze true
            int numInstancesTrue = TrainingAndTestingSet.getNumInstancesTrue(training);
            // numero di instanze false
            int numInstancesFalse = training.numInstances() - numInstancesTrue;

            if (numInstancesTrue < numInstancesFalse && numInstancesTrue != 0) {
                parameter = ((double) numInstancesFalse - (double) numInstancesTrue) / numInstancesTrue * 100.0;
            } else if (numInstancesTrue >= numInstancesFalse && numInstancesFalse != 0) {
                parameter = ((double) numInstancesTrue - (double) numInstancesFalse) / numInstancesFalse * 100.0;
            }

            // "-P": percentuale di instanze Smote da creare
            String[] opts = new String[]{"-P", String.valueOf(parameter)};
            smote.setOptions(opts);
            smote.setInputFormat(training);

        } catch (Exception e) {
            Logger.errorLog("Errore SMOTE");
            System.exit(1);
        }

        return smote;
    }
}
