package entity;

import org.eclipse.jgit.revwalk.RevCommit;
import util.Utils;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticket {
    private TicketJira ticket;
    private List<RevCommit> commitList;
    private RevCommit lastCommit;
    private LocalDate lastDateCommit;
    private int numCommit;

    public Ticket(TicketJira ticket) {
        this.ticket = ticket;
    }

    public TicketJira getTicket() {
        return ticket;
    }

    public void setTicket(TicketJira ticket) {
        this.ticket = ticket;
    }

    public List<RevCommit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<RevCommit> commitList) {
        this.commitList = commitList;
    }

    public RevCommit getLastCommit() {
        addLastCommit();
        return lastCommit;
    }

    public void setLastCommit(RevCommit lastCommit) {
        this.lastCommit = lastCommit;
    }

    private void addLastCommit() {
        Map.Entry<RevCommit, Date> latestEntry = lastCommitDate();
        setLastCommit(latestEntry.getKey());
    }

    public LocalDate getLastDateCommit() {
        addLastDateCommit();
        return lastDateCommit;
    }

    public void setLastDateCommit(LocalDate lastDateCommit) {
        this.lastDateCommit = lastDateCommit;
    }

    private void addLastDateCommit() {
        Map.Entry<RevCommit, Date> latestEntry = lastCommitDate();
        setLastDateCommit(Utils.convertToLocalDate(latestEntry.getValue()));
    }

    private Map<RevCommit, Date> commitDate() {
        Map<RevCommit, Date> commitDate = new HashMap<>();
        for (RevCommit rev : this.getCommitList()) {
            commitDate.put(rev, rev.getCommitterIdent().getWhen());
        }
        return commitDate;
    }

    private Map.Entry<RevCommit, Date> lastCommitDate() {
        Map<RevCommit, Date> commitDate = commitDate();

        Map.Entry<RevCommit, Date> latestEntry = null;
        for (Map.Entry<RevCommit, Date> entry : commitDate.entrySet()) {
            if (latestEntry == null || entry.getValue().after(latestEntry.getValue())) {
                latestEntry = entry;
            }
        }
        return latestEntry;
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
                "idTicket=" + ticket +
                ", commitList=" + commitList +
                ", lastCommit=" + lastCommit +
                ", lastDateCommit=" + lastDateCommit +
                ", numCommit=" + numCommit +
                '}' + "\n";
    }
}
