package milestone_two.balancing;

import milestone_two.TrainingAndTestingSet;
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
        int trainingSetSize = training.size();
        Resample resample = null;
        try {

            resample = new Resample();
            resample.setInputFormat(training);

            //mi calcolo la percentuale della classe maggioritaria
            int numInstancesTrue = TrainingAndTestingSet.getNumInstancesTrue(training);
            double percentageTrue = ((double) numInstancesTrue / trainingSetSize) * 100;
            double percentageMajorityClass;

            if (numInstancesTrue > (trainingSetSize - numInstancesTrue)) {
                percentageMajorityClass = percentageTrue;
            } else {
                percentageMajorityClass = ((trainingSetSize - numInstancesTrue) / (double) trainingSetSize) * 100;
            }

            double doublePercentageMajorityClass = percentageMajorityClass * 2;

            String[] opts = new String[]{"-B", "1.0", "-Z", String.valueOf(doublePercentageMajorityClass)};
            resample.setOptions(opts);
        } catch (Exception e) {
            Logger.errorLog("Errore Oversampling");
            System.exit(1);
        }

        return resample;
    }
}
