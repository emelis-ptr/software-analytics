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

        initCommitsToRelease(releases, commitsMap);

        for (Ticket ticket : tickets) {
            for (int i = 0; i < releases.size(); i++) {
                mapCommitsToRelease(commitsMap, releases, ticket, i);
            }
        }

        // rimuove metà della release
        // si eliminano dopo perchè dato che si considerano solo la metà delle release i commit dei ticket che
        // avvengono dopo l'ultima metà della releaase venivano assegnata ad essa
        commitsMap.entrySet().removeIf(entry -> entry.getKey() > halfRelease);
        return commitsMap;
    }

    /**
     * Inizializza la map assegnado come chiave il numero della release
     *
     * @param releases:   lista delle release
     * @param commitsMap: map che ha come chiave il numero della release e come valore una lista di commit
     */
    private static void initCommitsToRelease(List<Release> releases, Map<Integer, List<RevCommit>> commitsMap) {
        for (int i = 0; i < releases.size(); i++) {
            commitsMap.put(i, new ArrayList<>());
        }
    }

    /**
     * Per ogni ultimo commit del ticket determiniamo la release di appartenenza
     *
     * @param commitsMap:   map che ha come chiave il numero della release e come valore una lista di commit
     * @param releases:     lista della release
     * @param ticket:       ticket corrente
     * @param indexRelease: numero della release a partire dall'indice 0
     */
    private static void mapCommitsToRelease(Map<Integer, List<RevCommit>> commitsMap, List<Release> releases, Ticket ticket, int indexRelease) {
        LocalDate dateCurrentRelease = releases.get(indexRelease).getDateCreation();
        //Si trova alla prima release
        if (indexRelease == 0) {
            if (ticket.getLastDateCommit().isEqual(dateCurrentRelease) || ticket.getLastDateCommit().isBefore(dateCurrentRelease)) {
                commitsMap.get(indexRelease).add(ticket.getLastCommit());
            }
        } else {
            LocalDate datePreviousRelease = releases.get(indexRelease - 1).getDateCreation();
            //Se la data del commit si trova dopo la release precedente e uguale/prima delle release corrente
            //allora si inserire nella data corrente
            if (ticket.getLastDateCommit().isAfter(datePreviousRelease)
                    && (ticket.getLastDateCommit().isEqual(dateCurrentRelease) || ticket.getLastDateCommit().isBefore(dateCurrentRelease))) {
                commitsMap.get(indexRelease).add(ticket.getLastCommit());
            }
        }
    }


}
