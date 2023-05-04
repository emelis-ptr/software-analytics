package entity;

import java.util.ArrayList;
import java.util.List;

public class Dataset implements Comparable<Dataset> {
    private Release release;
    private File file;
    private int sizeLoc;
    private int locTouched;
    private int locTouchedFromR0;
    private int numR;
    private int numRFromR0;
    private int numFix;
    private int numFixFromR0;
    private List<String> authors;
    private int numAuth;
    private int numAuthFromR0;
    private int locAdded;
    private int locAddedFromR0;
    private int maxLocAdded;
    private int maxLocAddedFromR0;
    private float avgLocAdded;
    private float avgLocAddedFromR0;
    private int churn;
    private int churnFromR0;
    private int maxChurn;
    private int maxChurnFromR0;
    private float avgChurn;
    private float avgChurnFromR0;
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

    public int getLocTouchedFromR0() {
        return locTouchedFromR0;
    }

    public void setLocTouchedFromR0(int locTouchedFromR0) {
        this.locTouchedFromR0 = locTouchedFromR0;
    }

    public int getNumR() {
        return numR;
    }

    public void setNumR(int numR) {
        this.numR = numR;
    }

    public int getNumRFromR0() {
        return numRFromR0;
    }

    public void setNumRFromR0(int numRFromR0) {
        this.numRFromR0 = numRFromR0;
    }

    public int getNumFix() {
        return numFix;
    }

    public void setNumFix(int numFix) {
        this.numFix = numFix;
    }

    public int getNumFixFromR0() {
        return numFixFromR0;
    }

    public void setNumFixFromR0(int numFixFromR0) {
        this.numFixFromR0 = numFixFromR0;
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

    public int getNumAuthFromR0() {
        return numAuthFromR0;
    }

    public void setNumAuthFromR0(int numAuthFromR0) {
        this.numAuthFromR0 = numAuthFromR0;
    }

    public int getLocAdded() {
        return locAdded;
    }

    public void setLocAdded(int locAdded) {
        this.locAdded += locAdded;
    }

    public int getLocAddedFromR0() {
        return locAddedFromR0;
    }

    public void setLocAddedFromR0(int locAddedFromR0) {
        this.locAddedFromR0 = locAddedFromR0;
    }

    public int getMaxLocAdded() {
        return maxLocAdded;
    }

    public void setMaxLocAdded(int maxLocAdded) {
        this.maxLocAdded = maxLocAdded;
    }

    public int getMaxLocAddedFromR0() {
        return maxLocAddedFromR0;
    }

    public void setMaxLocAddedFromR0(int maxLocAddedFromR0) {
        this.maxLocAddedFromR0 = maxLocAddedFromR0;
    }

    public float getAvgLocAdded() {
        return avgLocAdded;
    }

    public void setAvgLocAdded(float avgLocAdded) {
        this.avgLocAdded = avgLocAdded;
    }

    public float getAvgLocAddedFromR0() {
        return avgLocAddedFromR0;
    }

    public void setAvgLocAddedFromR0(float avgLocAddedFromR0) {
        this.avgLocAddedFromR0 = avgLocAddedFromR0;
    }

    public int getChurn() {
        return churn;
    }

    public void setChurn(int churn) {
        this.churn += churn;
    }

    public int getChurnFromR0() {
        return churnFromR0;
    }

    public void setChurnFromR0(int churnFromR0) {
        this.churnFromR0 = churnFromR0;
    }

    public int getMaxChurn() {
        return maxChurn;
    }

    public void setMaxChurn(int maxChurn) {
        this.maxChurn = maxChurn;
    }

    public int getMaxChurnFromR0() {
        return maxChurnFromR0;
    }

    public void setMaxChurnFromR0(int maxChurnFromR0) {
        this.maxChurnFromR0 = maxChurnFromR0;
    }

    public float getAvgChurn() {
        return avgChurn;
    }

    public void setAvgChurn(float avgChurn) {
        this.avgChurn = avgChurn;
    }

    public float getAvgChurnFromR0() {
        return avgChurnFromR0;
    }

    public void setAvgChurnFromR0(float avgChurnFromR0) {
        this.avgChurnFromR0 = avgChurnFromR0;
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
                ", locTouchedTot=" + locTouchedFromR0 +
                ", numR=" + numR +
                ", numRTot=" + numRFromR0 +
                ", numFix=" + numFix +
                ", numFixTot=" + numFixFromR0 +
                ", authors=" + authors +
                ", numAuth=" + numAuth +
                ", numAuthTot=" + numAuthFromR0 +
                ", locAdded=" + locAdded +
                ", locAddedTot=" + locAddedFromR0 +
                ", maxLocAdded=" + maxLocAdded +
                ", maxLocAddedTot=" + maxLocAddedFromR0 +
                ", avgLocAdded=" + avgLocAdded +
                ", avgLocAddedTot=" + avgLocAddedFromR0 +
                ", churn=" + churn +
                ", churnTot=" + churnFromR0 +
                ", maxChurn=" + maxChurn +
                ", maxChurnTot=" + maxChurnFromR0 +
                ", avgChurn=" + avgChurn +
                ", avgChurnTot=" + avgChurnFromR0 +
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
