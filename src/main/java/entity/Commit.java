package entity;

import org.eclipse.jgit.revwalk.RevCommit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commit {

    private String shaId;
    private RevCommit revCommit;
    private String message;
    private TicketJira ticket;
    private LocalDate date;
    private String author;
    private Release release;
    private List<File> files;
    private Integer numFiles;

    public Commit(String shaId, RevCommit revCommit, String message, LocalDate date, String author) {
        this.shaId = shaId;
        this.revCommit = revCommit;
        this.message = message;
        this.date = date;
        this.author = author;
        this.files = new ArrayList<>();
    }

    public String getShaId() {
        return shaId;
    }

    public void setShaId(String shaId) {
        this.shaId = shaId;
    }

    public RevCommit getRevCommit() {
        return revCommit;
    }

    public void setRevCommit(RevCommit revCommit) {
        this.revCommit = revCommit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TicketJira getTicket() {
        return ticket;
    }

    public void setTicket(TicketJira ticket) {
        this.ticket = ticket;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Integer getNumFiles() {
        addNumFiles();
        return numFiles;
    }

    public void setNumFiles(Integer numFiles) {
        this.numFiles = numFiles;
    }

    public void addFile(File nameFile) {
        this.getFiles().add(nameFile);
    }

    private void addNumFiles() {
        if (!this.files.isEmpty()) {
            this.setNumFiles(this.getFiles().size());
        }
    }

    @Override
    public String toString() {
        return "Commit{" +
                "shaId='" + shaId + '\'' +
                ", commit=" + revCommit +
                ", message='" + message + '\'' +
                ", ticket='" + ticket.getIdTicket() + '\'' +
                ", date=" + date +
                ", author='" + author + '\'' +
                ", version=" + release +
                ", numFiles=" + numFiles +
                '}';
    }
}
