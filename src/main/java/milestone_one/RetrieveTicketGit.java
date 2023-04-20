package milestone_one;

import entity.Commit;
import entity.Release;
import entity.Ticket;
import entity.TicketJira;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import util.GitHandler;
import util.Utils;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RetrieveTicketGit {

    private RetrieveTicketGit() {
    }

    /**
     * Recupera i ticket il cui id risulta essere all'interno di un commit
     * tramite git
     *
     * @param commits: lista dei commits
     * @return: lista ticket
     */
    public static List<Ticket> retrieveTicketGit(List<Commit> commits) {
        List<Ticket> tickets = new ArrayList<>();

        commits.stream().filter(commit -> commit.getTicket() != null)
                .collect(Collectors.groupingBy(Commit::getTicket)).forEach((key, value) -> {
                    Ticket ticket;
                    ticket = new Ticket(key);

                    value.forEach(ticket::addCommit);
                    tickets.add(ticket);
                });

        return tickets;
    }

    /**
     * Recupera tutti i commit associato al progetto
     *
     * @throws GitAPIException:
     * @throws IOException:
     * @return: lista dei commit
     */
    public static List<Commit> getCommits(List<TicketJira> ticketJiras, List<Release> releases) throws GitAPIException, IOException {
        List<Commit> commits = new ArrayList<>();

        Iterable<RevCommit> logs = GitHandler.logsCommits();

        // aggiunge alla lista tutti i commit con le sue informazioni
        for (RevCommit rev : logs) {
            commits.add(new Commit(rev.getName(), rev, rev.getShortMessage(), rev.getCommitterIdent().getWhen().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate(), rev.getAuthorIdent().getName()));
        }
        // trova nella lista dei commit quelli che contengono l'id del ticket
        findFixedTicket(ticketJiras, commits, releases);
        return commits;
    }

    /**
     * Trova i commit che contengono nel suo log l'id del ticket
     *
     * @param ticketJiras: lista dei ticket presenti su Jira
     * @param commits:     lista dei commit del progetto
     * @param releases:    lista delle release
     */
    private static void findFixedTicket(List<TicketJira> ticketJiras, List<Commit> commits, List<Release> releases) {
        for (TicketJira ticketJira : ticketJiras) {
            commits.stream()
                    .filter(commit -> Utils.isContained(commit.getMessage(), ticketJira.getIdTicket()))
                    .forEach(commit -> commit.setTicket(ticketJira));
        }
        // trova la versione associata al commit
        findVersionTicket(commits, releases);
    }

    /**
     * Trova la release di appartenenza del commit
     *
     * @param commits:  lista dei commit
     * @param releases: lista delle release
     */
    private static void findVersionTicket(List<Commit> commits, List<Release> releases) {
        for (int i = 0; i < releases.size(); i++) {
            ReleaseCommits.commitsToRelease(releases, commits, i);
        }
    }
}
