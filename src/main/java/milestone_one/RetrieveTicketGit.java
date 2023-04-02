package milestone_one;

import entity.Ticket;
import entity.TicketJira;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import util.Repo;
import util.Utils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RetrieveTicketGit {

    private RetrieveTicketGit() {
    }

    /**
     * Recupera i ticket il cui id risulta essere all'interno di un commit
     * tramite git
     *
     * @param ticketJiras: lista dei ticket presenti su Jira
     * @throws IOException:
     * @throws GitAPIException;
     * @return: lista ticket
     */
    public static List<Ticket> retrieveTicketGit(List<TicketJira> ticketJiras) throws IOException, GitAPIException {
        List<Ticket> tickets = new ArrayList<>();

        Repository repository = Repo.repository(MilestoneOne.PATH_PROJ);
        Git git = new Git(repository);
        Iterable<RevCommit> logs = git.log().call();

        // si crea una mappa <Commit, IdTicket>
        Map<RevCommit, String> ticketsCommit = new HashMap<>();
        // per ogni commit verifica che l'id del ticket si trovi all'interno del log di un commit
        for (RevCommit rev : logs) {
            for (TicketJira ticketJira : ticketJiras) {
                if (Utils.isContained(rev.getFullMessage(), ticketJira.getIdTicket())) {
                    ticketsCommit.put(rev, ticketJira.getIdTicket());
                }
            }
        }

        // raggruppa i commit per id del ticket
        Map<String, List<RevCommit>> listTickets = ticketsCommit.keySet().stream().collect(Collectors.groupingBy(ticketsCommit::get, TreeMap::new, Collectors.toList()));
        createTicket(tickets, ticketJiras, listTickets);

        return tickets;
    }

    /**
     * Crea entità Ticket
     *
     * @param tickets:     lista dei ticket
     * @param ticketJiras: liste dei ticket presenti su Jira
     * @param commits:     lista dei commit di un progetto
     */
    private static void createTicket(List<Ticket> tickets, List<TicketJira> ticketJiras, Map<String, List<RevCommit>> commits) {
        for (TicketJira version : ticketJiras) {
            if (commits.containsKey(version.getIdTicket())) {
                tickets.add(new Ticket(version));
                tickets.get(tickets.size() - 1).setCommitList(commits.get(version.getIdTicket()));
            }
        }
    }
}
