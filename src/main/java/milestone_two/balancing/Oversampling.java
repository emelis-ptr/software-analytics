package milestone_two.balancing;

import milestone_two.TrainingAndTestingSet;
import util.MyLogger;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.supervised.instance.Resample;

import static enums.Balance.OVERSAMPLING;

public class Oversampling extends Balancing {

    public Oversampling(Instances training, Instances testing) {
        super(training, testing);
        this.nameBalancing = OVERSAMPLING;
        oversampling();
    }

    /**
     * Oversampling
     */
    public void oversampling() {
        int trainingSize = training.numInstances();
        this.filterClassifier = new FilteredClassifier();

        try {
            Resample resample = new Resample();
            resample.setInputFormat(training);

            //mi calcolo la percentuale della classe minoritaria
            int numInstancesTrue = TrainingAndTestingSet.getNumInstancesTrue(training);
            double percentageTrue = ((double) numInstancesTrue / trainingSize) * 100;
            double percentageMajorityClass;

            if (numInstancesTrue > (trainingSize - numInstancesTrue)) {
                percentageMajorityClass = percentageTrue;
            } else {
                percentageMajorityClass = ((trainingSize - numInstancesTrue) / (double) trainingSize) * 100;
            }

            double doublePercentageMajorityClass = percentageMajorityClass * 2;

            String[] opts = new String[]{"-B", "1.0", "-Z", String.valueOf(doublePercentageMajorityClass)};
            resample.setOptions(opts);
            resample.setNoReplacement(false);
            this.filterClassifier.setFilter(resample);
        } catch (Exception e) {
            MyLogger.errorLog("Errore Oversampling");
            System.exit(1);
        }
    }
}
