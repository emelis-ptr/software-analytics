package entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class File {

    private String nameFile;
    private Release release;
    private Commit commit;
    private LocalDate fileCreation;
    private int sizeLOC;
    private int age;
    private boolean isRenamed;

    public File(String nameFile, Release release) {
        this.nameFile = nameFile;
        this.release = release;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public LocalDate getFileCreation() {
        return fileCreation;
    }

    public void setFileCreation(LocalDate fileCreation) {
        this.fileCreation = fileCreation;
    }

    public int getSizeLOC() {
        return sizeLOC;
    }

    public void setSizeLOC(int sizeLOC) {
        this.sizeLOC = sizeLOC;
    }

    public int getAge() {
        if (this.getFileCreation() != null) {
            int a = (int) this.getFileCreation().until(this.getRelease().getDateCreation(), ChronoUnit.WEEKS);
            this.setAge(a);
        } else {
            this.setAge(0);
        }
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isRenamed() {
        return isRenamed;
    }

    public void setRenamed(boolean renamed) {
        isRenamed = renamed;
    }

    @Override
    public String toString() {
        return "File{" +
                "nameFile='" + nameFile + '\'' +
                ", release=" + release +
                ", commit=" + commit +
                ", fileCreation=" + fileCreation +
                ", sizeLOC=" + sizeLOC +
                ", age=" + age +
                ", isRenamed=" + isRenamed +
                '}';
    }
}
