package milestone_one;

import entity.Commit;
import entity.Dataset;
import entity.File;
import entity.Release;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.NullOutputStream;
import util.GitHandler;
import util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static util.Constants.JAVA_EXT;

public class BuildDataset {

    private BuildDataset() {
    }

    /**
     * Determina tutti i file per ogni ultimo commit di ogni release
     *
     * @return: dataset
     */
    public static List<Dataset> buildDataset(Map<Release, List<Commit>> mapReleaseCommits) {
        List<Dataset> dataset = new ArrayList<>();
        TreeWalk treeWalk = GitHandler.treeWalk();

        mapReleaseCommits.forEach((k, v) -> {
            File file;
            Commit commit = v.get(v.size() - 1);
            ObjectId treeId = commit.getRevCommit().getTree();
            try {
                treeWalk.reset(treeId);
                treeWalk.setRecursive(true);
                while (treeWalk.next()) {
                    // considera solo i file che hanno l'estensione ".java"
                    if (treeWalk.getPathString().endsWith(JAVA_EXT)) {
                        String nameFile = treeWalk.getPathString();
                        file = new File(nameFile, k);
                        file.setSizeLOC(Metric.calculateSize(treeWalk));
                        file.setCommit(commit);

                        dataset.add(new Dataset(file));
                        commit.addFile(file);
                    }
                }
            } catch (IOException e) {
                Logger.errorLog("Errore nell'albero del commit");
            }
        });
        return dataset;
    }

    /**
     * Controlla se ci sono delle differenze nell'albero del commit e inserisce le metriche di ogni file
     *
     * @param mapReleaseCommits: map con chiave-valore. key: release; value: list commit
     * @param dataset:           dataset
     * @throws IOException:
     */
    public static void findClassTouched(List<Dataset> dataset, Map<Release, List<Commit>> mapReleaseCommits) throws IOException {
        //diffFormatter trova le differenze da i tree di due commit (ovvero tra i file)
        DiffFormatter diffFormatter = new DiffFormatter(NullOutputStream.INSTANCE);
        diffFormatter.setRepository(GitHandler.repository(MilestoneOne.path()));
        diffFormatter.setDetectRenames(true);

        mapReleaseCommits.forEach((key, value1) -> value1.forEach(commit -> {
            RevCommit revCommit = commit.getRevCommit();
            RevCommit previousCommit = revCommit.getParentCount() > 0 ? revCommit.getParent(0) : null;

            if (previousCommit != null) {
                //diffFormatter restituisce una lista dei path dei file che sono differenti tra i due commit
                try {
                    for (DiffEntry entry : diffFormatter.scan(previousCommit, revCommit)) {
                        Metric.calculateMetrics(dataset, commit, entry, diffFormatter, key.getNumVersion());
                    }
                } catch (IOException e) {
                    Logger.errorLog("Errore in DiffFormatter");
                }
            }
        }));
    }

    /**
     * Si aggiungono i file di una release se non sono presenti nel dataset.
     * Li consideriamo perchè anche se non ci sono commit associati alla release, questi file sono comunque presenti
     *
     * @param mapReleaseCommits: map con chiave-valore. key: release; value: list commit
     * @param releases:          lista delle release
     * @param dataset:           dataset
     * @param halfRelease:       release da considerare
     */
    public static void addFilesToEmptyRelease(Map<Release, List<Commit>> mapReleaseCommits, List<Release> releases, List<Dataset> dataset, int halfRelease) {
        // determina release mancante e aggiunge la lista dei commit della release precedente
        releases.forEach(release -> {
            if (release.getNumVersion() < halfRelease && (!mapReleaseCommits.containsKey(release))) {
                List<Dataset> datasetFiltered = dataset.stream()
                        .filter(rowDataset -> rowDataset.getRelease().equals(releases.get(release.getNumVersion() - 2))).toList();

                datasetFiltered.forEach(rowDataset -> {
                    File newFile = new File(rowDataset.getFile().getNameFile(), release);
                    newFile.setSizeLOC(rowDataset.getFile().getSizeLOC());
                    dataset.add(new Dataset(newFile));
                });
            }
        });
        Collections.sort(dataset);
    }

}


