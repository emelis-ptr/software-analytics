package entity;

import org.eclipse.jgit.revwalk.RevCommit;
import util.Utils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Ticket {
    private TicketJira idTicket;
    private List<RevCommit> commitList;
    private LocalDate lastDateCommit;
    private int numCommit;

    public Ticket(TicketJira idTicket) {
        this.idTicket = idTicket;
    }

    public TicketJira getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(TicketJira idTicket) {
        this.idTicket = idTicket;
    }

    public List<RevCommit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<RevCommit> commitList) {
        this.commitList = commitList;
    }

    public LocalDate getLastDateCommit() {
        addLastCommit();
        return lastDateCommit;
    }

    public void setLastDateCommit(LocalDate lastDateCommit) {
        this.lastDateCommit = lastDateCommit;
    }

    private void addLastCommit() {
        Date lastCommitHash = this.getCommitList().get(0).getAuthorIdent().getWhen();
        setLastDateCommit(Utils.convertToLocalDate(lastCommitHash));
    }

    public int getNumCommit() {
        addNumCommit();
        return numCommit;
    }

    public void setNumCommit(int numCommit) {
        this.numCommit = numCommit;
    }

    private void addNumCommit() {
        setNumCommit(this.getCommitList().size());
    }


    @Override
    public String toString() {
        return "\n" + "Ticket{" +
                "idTicket=" + idTicket +
                ", commitList=" + commitList +
                ", lastDateCommit=" + lastDateCommit +
                ", numCommit=" + numCommit +
                '}' + "\n";
    }
}
