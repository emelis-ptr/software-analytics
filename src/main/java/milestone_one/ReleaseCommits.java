package milestone_one;

import entity.Release;
import entity.Ticket;
import org.eclipse.jgit.revwalk.RevCommit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReleaseCommits {

    private ReleaseCommits() {
    }

    /**
     * Per ogni ticket determina la release di appartenza del suo ultimo commit
     *
     * @param releases: lista delle release
     * @param tickets:  lista dei ticket
     * @return: una coppia < commit,numero intero> dove il numero interno è il numero di versione della release
     */
    public static Map<Integer, List<RevCommit>> findReleaseCommits(List<Release> releases, List<Ticket> tickets) {
        Map<Integer, List<RevCommit>> commitsMap = new HashMap<>();

        // si considerano solo metà delle release
        // -1 perchè nella map l'indice della release parte da 0
        int halfRelease = (releases.size() / 2) - 1;

        for (int i = 0; i < releases.size(); i++) {
            commitsMap.put(i, new ArrayList<>());
        }

        //per ogni ultimo commit del ticket determiniamo la release di appartenenza
        for (Ticket ticket : tickets) {
            for (int i = 0; i < releases.size(); i++) {
                LocalDate dateCurrentRelease = releases.get(i).getDateCreation();
                //Si trova alla prima release
                if (i == 0) {
                    if (ticket.getLastDateCommit().isEqual(dateCurrentRelease) || ticket.getLastDateCommit().isBefore(dateCurrentRelease)) {
                        commitsMap.get(i).add(ticket.getLastCommit());
                    }
                } else {
                    LocalDate datePreviousRelease = releases.get(i - 1).getDateCreation();
                    //Se la data del commit si trova dopo la release precedente e uguale/prima delle release corrente
                    //allora si inserire nella data corrente
                    if (ticket.getLastDateCommit().isAfter(datePreviousRelease)
                            && (ticket.getLastDateCommit().isEqual(dateCurrentRelease) || ticket.getLastDateCommit().isBefore(dateCurrentRelease))) {
                        commitsMap.get(i).add(ticket.getLastCommit());
                    }
                }
            }
        }

        // rimuove metà della release
        // si eliminano dopo perchè dato che si considerano solo la metà delle release i commit dei ticket che
        // avvengono dopo l'ultima metà della releaase venivano assegnata ad essa
        commitsMap.entrySet().removeIf(entry -> entry.getKey() > halfRelease);
        return commitsMap;
    }

}
