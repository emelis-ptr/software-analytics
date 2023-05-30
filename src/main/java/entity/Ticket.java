package entity;

import util.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ticket {
    private List<Commit> commitList;
    private Commit lastCommit;
    private LocalDate lastDateCommit;
    private int numCommit;
    private boolean contained; // se ha il ticket nel commit

    public Ticket() {
        this.commitList = new ArrayList<>();
    }

    public List<Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<Commit> commitList) {
        this.commitList = commitList;
    }

    public Commit getLastCommit() {
        addLastCommit();
        return lastCommit;
    }

    public void setLastCommit(Commit lastCommit) {
        this.lastCommit = lastCommit;
    }

    private void addLastCommit() {
        this.commitList.sort(Comparator.comparing(d -> d.getRevCommit().getCommitterIdent().getWhen()));
        setLastCommit(this.commitList.get(this.commitList.size() - 1));
    }

    public LocalDate getLastDateCommit() {
        addLastDateCommit();
        return lastDateCommit;
    }

    public void setLastDateCommit(LocalDate lastDateCommit) {
        this.lastDateCommit = lastDateCommit;
    }

    private void addLastDateCommit() {
        this.commitList.sort(Comparator.comparing(d -> d.getRevCommit().getCommitterIdent().getWhen()));
        setLastDateCommit(Utils.convertToLocalDate(this.commitList.get(this.commitList.size() - 1).getRevCommit().getCommitterIdent().getWhen()));
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

    public void addCommit(Commit commit) {
        if (!this.commitList.contains(commit)) {
            this.commitList.add(commit);
        }
    }

    public boolean isContained() {
        return contained;
    }

    public void setContained(boolean contained) {
        this.contained = contained;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "commitList=" + commitList +
                ", lastCommit=" + lastCommit +
                ", lastDateCommit=" + lastDateCommit +
                ", numCommit=" + numCommit +
                ", contained=" + contained +
                '}';
    }
}
