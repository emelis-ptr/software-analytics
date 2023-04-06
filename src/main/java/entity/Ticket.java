package entity;

import org.eclipse.jgit.revwalk.RevCommit;
import util.Utils;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticket {
    private TicketJira ticketJira;
    private List<RevCommit> commitList;
    private RevCommit lastCommit;
    private LocalDate lastDateCommit;
    private int numCommit;

    public Ticket(TicketJira ticketJira) {
        this.ticketJira = ticketJira;
    }

    public TicketJira getTicketJira() {
        return ticketJira;
    }

    public void setTicketJira(TicketJira ticketJira) {
        this.ticketJira = ticketJira;
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
        Map<RevCommit, Date> latestEntry = lastCommitDate();
        setLastCommit(latestEntry.keySet().stream().toList().get(0));
    }

    public LocalDate getLastDateCommit() {
        addLastDateCommit();
        return lastDateCommit;
    }

    public void setLastDateCommit(LocalDate lastDateCommit) {
        this.lastDateCommit = lastDateCommit;
    }

    private void addLastDateCommit() {
        Map<RevCommit, Date> latestEntry = lastCommitDate();
        setLastDateCommit(Utils.convertToLocalDate(latestEntry.values().stream().toList().get(0)));
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
                "idTicket=" + ticketJira +
                ", commitList=" + commitList +
                ", lastCommit=" + lastCommit +
                ", lastDateCommit=" + lastDateCommit +
                ", numCommit=" + numCommit +
                '}' + "\n";
    }

    private Map<RevCommit, Date> commitDate() {
        Map<RevCommit, Date> commitDate = new HashMap<>();
        for (RevCommit rev : this.getCommitList()) {
            commitDate.put(rev, rev.getCommitterIdent().getWhen());
        }
        return commitDate;
    }

    private Map<RevCommit, Date> lastCommitDate() {
        Map<RevCommit, Date> commitDate = commitDate();

        Map<RevCommit, Date> latestEntry = new HashMap<>();
        for (Map.Entry<RevCommit, Date> entry : commitDate.entrySet()) {
            if (latestEntry.isEmpty()) {
                latestEntry.put(entry.getKey(), entry.getValue());
            } else {
                if (entry.getValue().after(latestEntry.values().stream().toList().get(0))) {
                    latestEntry.clear();
                    latestEntry.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return latestEntry;
    }
}
