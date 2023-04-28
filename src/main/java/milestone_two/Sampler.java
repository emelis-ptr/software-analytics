package milestone_two;

import entity.Result;
import util.Logger;
import weka.core.Instances;
import weka.filters.supervised.instance.Resample;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.supervised.instance.SpreadSubsample;

public class Sampler {

    private Sampler() {
    }

    private static String[] opts = new String[]{};

    /**
     * Undersampling
     *
     * @param instances:
     * @return:
     */
    public static SpreadSubsample undersampling(Instances instances) {

        SpreadSubsample spreadSubsample = null;
        opts = new String[]{"-M", "1.0"};
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
        int trainingSetSize = training.size();

        Resample resample = null;
        try {

            resample = new Resample();
            resample.setInputFormat(training);

            //mi calcolo la percentuale della classe maggioritaria
            int numInstancesTrue = Result.getNumInstancesTrue(training);
            double percentageTrue = ((double) numInstancesTrue / trainingSetSize) * 100;
            double percentageMajorityClass;

            if (numInstancesTrue > (trainingSetSize - numInstancesTrue)) {
                percentageMajorityClass = percentageTrue;
            } else {
                percentageMajorityClass = ((trainingSetSize - numInstancesTrue) / (double) trainingSetSize) * 100;
            }

            double doublePercentageMajorityClass = percentageMajorityClass * 2;

            opts = new String[]{"-B", "1.0", "-Z", String.valueOf(doublePercentageMajorityClass)};
            resample.setOptions(opts);

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
            double parameter = 0;
            int numInstancesTrue = Result.getNumInstancesTrue(training);
            int numInstancesFalse = training.numInstances() - numInstancesTrue;
            if (numInstancesTrue < numInstancesFalse && numInstancesTrue != 0) {
                parameter = ((double) numInstancesFalse - (double) numInstancesTrue) / numInstancesTrue * 100.0;
            } else if (numInstancesTrue >= numInstancesFalse && numInstancesFalse != 0) {
                parameter = ((double) numInstancesTrue - (double) numInstancesFalse) / numInstancesFalse * 100.0;
            }

            opts = new String[]{"-P", String.valueOf(parameter)};
            smote.setOptions(opts);
            smote.setInputFormat(training);

        } catch (Exception e) {
            Logger.errorLog("Errore SMOTE");
            System.exit(1);
        }

        return smote;

    }
}
