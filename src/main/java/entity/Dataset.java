package entity;

import java.util.ArrayList;
import java.util.List;

public class Dataset implements Comparable<Dataset> {
    private Release release;
    private File file;
    private int sizeLoc;
    private int locTouched;
    private int locTouchedTot;
    private int numR;
    private int numRTot;
    private int numFix;
    private int numFixTot;
    private List<String> authors;
    private int numAuth;
    private int numAuthTot;
    private int locAdded;
    private int maxLocAdded;
    private float avgLocAdded;
    private int churn;
    private int maxChurn;
    private float avgChurn;
    private int age;
    private double weightedAge;
    private boolean isBuggy;

    public Dataset(File file) {
        this.file = file;
        this.authors = new ArrayList<>();
    }

    public Dataset() {
    }

    public Release getRelease() {
        this.setRelease(this.getFile().getRelease());
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getSizeLoc() {
        addSizeLOC();
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

    public int getLocTouchedTot() {
        return locTouchedTot;
    }

    public void setLocTouchedTot(int locTouchedTot) {
        this.locTouchedTot = locTouchedTot;
    }

    public int getNumR() {
        return numR;
    }

    public void setNumR(int numR) {
        this.numR = numR;
    }

    public int getNumRTot() {
        return numRTot;
    }

    public void setNumRTot(int numRTot) {
        this.numRTot = numRTot;
    }

    public int getNumFix() {
        return numFix;
    }

    public void setNumFix(int numFix) {
        this.numFix = numFix;
    }

    public int getNumFixTot() {
        return numFixTot;
    }

    public void setNumFixTot(int numFixTot) {
        this.numFixTot = numFixTot;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public int getNumAuth() {
        addNumAuthors();
        return numAuth;
    }

    public void setNumAuth(int numAuth) {
        this.numAuth = numAuth;
    }

    public int getNumAuthTot() {
        return numAuthTot;
    }

    public void setNumAuthTot(int numAuthTot) {
        this.numAuthTot = numAuthTot;
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

    public int getAge() {
        this.setAge(this.getFile().getAge());
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

    public void addAuthors(String authors) {
        if (!this.authors.contains(authors)) {
            this.authors.add(authors);
        }
    }

    private void addNumAuthors() {
        if (!this.authors.isEmpty()) {
            this.setNumAuth(this.authors.size());
        } else {
            this.setNumAuth(0);
        }
    }

    private void addSizeLOC() {
        this.setSizeLoc(this.getFile().getSizeLOC());
    }


    @Override
    public String toString() {
        return "Dataset{" +
                "release=" + release.getNumVersion() +
                ", file=" + file.getNameFile() +
                ", sizeLoc=" + sizeLoc +
                ", locTouched=" + locTouched +
                ", locTouchedTot=" + locTouchedTot +
                ", numR=" + numR +
                ", numRTot=" + numRTot +
                ", numFix=" + numFix +
                ", numFixTot=" + numFixTot +
                ", numAuth=" + numAuth +
                ", numAuthTot=" + numAuthTot +
                ", locAdded=" + locAdded +
                ", maxLocAdded=" + maxLocAdded +
                ", avgLocAdded=" + avgLocAdded +
                ", churn=" + churn +
                ", maxChurn=" + maxChurn +
                ", avgChurn=" + avgChurn +
                ", age=" + age +
                ", weightedAge=" + weightedAge +
                ", isBuggy=" + isBuggy +
                '}';
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

    @Override
    public int hashCode() {
        return this.getRelease().hashCode();
    }

}
