package milestone_one;

import entity.Commit;
import entity.Release;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReleaseCommits {

    private ReleaseCommits() {
    }

    /**
     * Per ogni commit del ticket determiniamo la release di appartenenza
     *
     * @param releases:     lista della release
     * @param commits:      lista dei commits
     * @param indexRelease: numero della release a partire dall'indice 0
     */
    public static void commitsToRelease(List<Release> releases, List<Commit> commits, int indexRelease) {
        LocalDate dateCurrentRelease = releases.get(indexRelease).getDateCreation();

        commits.forEach(commit -> {
            LocalDate dateCommit = commit.getDate();
            if (indexRelease == 0) {
                if (dateCommit.isEqual(dateCurrentRelease) || dateCommit.isBefore(dateCurrentRelease)) {
                    commit.setRelease(releases.get(indexRelease));
                }
            } else {
                LocalDate datePreviousRelease = releases.get(indexRelease - 1).getDateCreation();
                if (dateCommit.isAfter(datePreviousRelease)
                        && (dateCommit.isEqual(dateCurrentRelease) || dateCommit.isBefore(dateCurrentRelease))) {
                    commit.setRelease(releases.get(indexRelease));
                }
            }
        });

        commits.stream().filter(commit -> commit.getRelease() == null)
                .forEach(commit -> commit.setRelease(releases.get(releases.size() - 1)));
    }

    /**
     * Crea una mappa <Release, List<Commit>>
     *
     * @param commits:     lista dei commit
     * @param releases:    lista delle release
     * @param halfRelease: release da considerare, in questo caso la metà
     * @return:
     */
    public static Map<Release, List<Commit>> mapReleaseCommits(List<Commit> commits, List<Release> releases, int halfRelease) {
        Map<Release, List<Commit>> treeMap = new TreeMap<>(Comparator.comparing(Release::getNumVersion));
        treeMap.putAll(commits.stream()
                .collect(Collectors.groupingBy(Commit::getRelease)));

        // ordina per ogni chiave la lista dei commit per data
        treeMap.forEach((key, value) -> value.sort(Comparator.comparing(d -> d.getRevCommit().getCommitterIdent().getWhen())));
        // determina release mancante e aggiunge la lista dei commit della release precedente
        releases.forEach(release -> {
            if (!treeMap.containsKey(release)) {
                treeMap.put(release, treeMap.get(releases.get(release.getNumVersion() - 2)));
            }
        });

        treeMap.entrySet().removeIf(entry -> entry.getKey().getNumVersion() > halfRelease);
        return treeMap;
    }

}
