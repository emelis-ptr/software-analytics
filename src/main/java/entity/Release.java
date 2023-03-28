package entity;

import java.time.LocalDate;

public class Release {
    private Integer numVersion;
    private String releaseID;
    private String releaseName;
    private LocalDate dateCreation;

    public Release(String releaseID, String releaseName, LocalDate dateCreation) {
        this.releaseID = releaseID;
        this.releaseName = releaseName;
        this.dateCreation = dateCreation;
    }

    public Integer getNumVersion() {
        return numVersion;
    }

    public void setNumVersion(Integer numVersion) {
        this.numVersion = numVersion;
    }

    public String getReleaseID() {
        return releaseID;
    }

    public void setReleaseID(String releaseID) {
        this.releaseID = releaseID;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "\n" + "entity.Release{" +
                "numVersion=" + numVersion +
                ", releaseID='" + releaseID + '\'' +
                ", releaseName='" + releaseName + '\'' +
                ", dateCreation=" + dateCreation +
                '}' + "\n";
    }
}
