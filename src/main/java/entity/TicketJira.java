package entity;

import java.time.LocalDate;
import java.util.List;

public class TicketJira {
    private String idTicket;
    private LocalDate creationDate;
    private LocalDate resolutionDate;
    private Release injectedVersion;
    private Release openingVersion;
    private Release fixedVersion;
    private List<Release> affectedVersion;

    public TicketJira(String idTicket, LocalDate creationDate, LocalDate resolutionDate) {
        this.idTicket = idTicket;
        this.creationDate = creationDate;
        this.resolutionDate = resolutionDate;
    }

    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(LocalDate resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public Release getInjectedVersion() {
        return injectedVersion;
    }

    public void setInjectedVersion(Release injectedVersion) {
        this.injectedVersion = injectedVersion;
    }

    public Release getOpeningVersion() {
        return openingVersion;
    }

    public void setOpeningVersion(Release openingVersion) {
        this.openingVersion = openingVersion;
    }

    public Release getFixedVersion() {
        return fixedVersion;
    }

    public void setFixedVersion(Release fixedVersion) {
        this.fixedVersion = fixedVersion;
    }

    public List<Release> getAffectedVersion() {
        return affectedVersion;
    }

    public void setAffectedVersion(List<Release> affectedVersion) {
        this.affectedVersion = affectedVersion;
    }

    @Override
    public String toString() {
        return "Version{" +
                "idTicket='" + idTicket + '\'' +
                ", creationDate=" + creationDate +
                ", resolutionDate=" + resolutionDate +
                ", injectedVersion=" + injectedVersion +
                ", openingVersion=" + openingVersion +
                ", fixedVersion=" + fixedVersion +
                ", affectedVersion=" + affectedVersion +
                '}';
    }
}
