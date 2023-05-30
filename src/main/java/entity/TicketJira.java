package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketJira extends Ticket{
    private String idTicket;
    private LocalDate creationDate;
    private LocalDate resolutionDate;
    private Release injectedVersion;
    private Release openingVersion;
    private Release fixedVersion;
    private List<Release> affectedVersion;

    public TicketJira(String idTicket, LocalDate creationDate, LocalDate resolutionDate) {
        super();
        this.idTicket = idTicket;
        this.creationDate = creationDate;
        this.resolutionDate = resolutionDate;
        this.affectedVersion = new ArrayList<>();
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

    public void addAffectedVersion(Release av){
        if (!this.affectedVersion.contains(av)){
            this.affectedVersion.add(av);
        }
    }

    /***
     * Per ogni Ticket inserisce FV se l'ultima data del commit in git è uguale
     * o avviene dopo la data di release.
     * Per ogni Ticket inserisce FV se non trova il commit associato tramite resolutionDate
     *
     * @param releases: lista delle release
     **/
    public void setFV(List<Release> releases) {
        for (Release release : releases) {
            if (getLastDateCommit().equals(release.getDateCreation()) || release.getDateCreation().isAfter(getLastDateCommit())) {
                setFixedVersion(release);
                break;
            }
        }
        if (getFixedVersion() == null) {
            for (Release release : releases) {
                if (getResolutionDate().equals(release.getDateCreation()) || release.getDateCreation().isAfter(getResolutionDate())) {
                    setFixedVersion(release);
                    break;
                } else {
                    setFixedVersion(releases.get(releases.size() - 1));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "TicketJira{" +
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
