package entity;

public class Dataset implements Comparable<Dataset> {
    private Release release;
    private String nameFile;
    private int sizeLoc;
    private int locTouched;
    private int numR;
    private int numFix;
    private int numAuth;
    private int locAdded;
    private int maxLocAdded;
    private float avgLocAdded;
    private int churn;
    private int maxChurn;
    private float avgChurn;
    private int chgSetSize;
    private int maxChgSet;
    private float avgChgSet;
    private int age;
    private double weightedAge;
    private boolean isBuggy;

    public Dataset(Release release, String nameFile) {
        this.release = release;
        this.nameFile = nameFile;
    }

    public Dataset() {
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public int getSizeLoc() {
        return sizeLoc;
    }

    public void setSizeLoc(int sizeLoc) {
        this.sizeLoc = sizeLoc;
    }

    public int getLocTouched() {
        return locTouched;
    }

    public void setLocTouched(int locTouched) {
        this.locTouched += locTouched;
    }

    public int getNumR() {
        return numR;
    }

    public void setNumR(int numR) {
        this.numR = numR;
    }

    public int getNumFix() {
        return numFix;
    }

    public void setNumFix(int numFix) {
        this.numFix = numFix;
    }

    public int getNumAuth() {
        return numAuth;
    }

    public void setNumAuth(int numAuth) {
        this.numAuth = numAuth;
    }

    public int getLocAdded() {
        return locAdded;
    }

    public void setLocAdded(int locAdded) {
        this.locAdded += locAdded;
    }

    public int getMaxLocAdded() {
        return maxLocAdded;
    }

    public void setMaxLocAdded(int maxLocAdded) {
        this.maxLocAdded = maxLocAdded;
    }

    public float getAvgLocAdded() {
        return avgLocAdded;
    }

    public void setAvgLocAdded(float avgLocAdded) {
        this.avgLocAdded = avgLocAdded;
    }

    public int getChurn() {
        return churn;
    }

    public void setChurn(int churn) {
        this.churn += churn;
    }

    public int getMaxChurn() {
        return maxChurn;
    }

    public void setMaxChurn(int maxChurn) {
        this.maxChurn = maxChurn;
    }

    public float getAvgChurn() {
        return avgChurn;
    }

    public void setAvgChurn(float avgChurn) {
        this.avgChurn = avgChurn;
    }

    public int getChgSetSize() {
        return chgSetSize;
    }

    public void setChgSetSize(int chgSetSize) {
        this.chgSetSize = chgSetSize;
    }

    public int getMaxChgSet() {
        return maxChgSet;
    }

    public void setMaxChgSet(int maxChgSet) {
        this.maxChgSet = maxChgSet;
    }

    public float getAvgChgSet() {
        return avgChgSet;
    }

    public void setAvgChgSet(float avgChgSet) {
        this.avgChgSet = avgChgSet;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeightedAge() {
        addWeightedAge();
        return weightedAge;
    }

    public void setWeightedAge(double weightedAge) {
        this.weightedAge = weightedAge;
    }

    public boolean isBuggy() {
        return isBuggy;
    }

    public void setBuggy(boolean buggy) {
        isBuggy = buggy;
    }

    private void addWeightedAge() {
        if (this.getLocTouched() != 0) {
            double weight = (double) this.getAge() / (double) this.getLocTouched();
            setWeightedAge(weight);
        }
    }

    @Override
    public String toString() {
        return " ** Metrics ** {" +
                "entity.Release=" + release.getNumVersion() +
                ", fileName= " + nameFile +
                ", sizeFile=" + sizeLoc +
                ", locTouched=" + locTouched +
                ", numR=" + numR +
                ", numFix=" + numFix +
                ", numAuth=" + numAuth +
                ", locAdded=" + locAdded +
                ", maxLocAdded=" + maxLocAdded +
                ", avgLocAdded=" + avgLocAdded +
                ", churn=" + churn +
                ", maxChurn=" + maxChurn +
                ", avgChurn=" + avgChurn +
                ", chgSetSize=" + chgSetSize +
                ", maxChgSet=" + maxChgSet +
                ", avgChgset=" + avgChgSet +
                ", age=" + age +
                ", weightedAge=" + weightedAge +
                ", isBuggy=" + isBuggy +
                '}' + "\n";
    }

    @Override
    public int compareTo(Dataset dataset) {
        return this.getRelease().getNumVersion().compareTo(dataset.getRelease().getNumVersion());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        // nessuna istanza dovrebbe essere uguale a null, quindi andiamo a verificarlo
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Dataset dataset = (Dataset) obj;
        return this.getRelease().getNumVersion().equals(dataset.release.getNumVersion());
    }
}
