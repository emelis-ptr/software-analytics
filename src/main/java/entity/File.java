package entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class File {

    private String nameFile;
    private String oldNameFile;
    private Release release;
    private Commit commit;
    private LocalDate fileCreation;
    private int sizeLOC;
    private int age;
    private boolean isRenamed;
    private boolean isTouched;
    private boolean isModified;

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

    public String getOldNameFile() {
        return oldNameFile;
    }

    public void setOldNameFile(String oldNameFile) {
        this.oldNameFile = oldNameFile;
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

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
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
                ", isTouched=" + isTouched +
                ", isModified=" + isModified +
                '}';
    }
}
