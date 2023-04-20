package entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class File {

    private String nameFile;
    private Release release;
    private Commit commit;
    private String oldNameFile;
    private LocalDate fileCreation;
    private int sizeLOC;
    private int age;

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

    public String getOldNameFile() {
        return oldNameFile;
    }

    public void setOldNameFile(String oldNameFile) {
        this.oldNameFile = oldNameFile;
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
        if(this.getFileCreation() != null) {
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

    @Override
    public String toString() {
        return "File{" +
                "nameFile='" + nameFile + '\'' +
                ", release=" + release +
                ", commit=" + commit +
                ", oldNameFile='" + oldNameFile + '\'' +
                ", fileCreation=" + fileCreation +
                ", sizeLOC=" + sizeLOC +
                ", age=" + age +
                '}';
    }
}