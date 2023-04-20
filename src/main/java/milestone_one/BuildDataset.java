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
import util.WriteCSV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.Constants.JAVA_EXT;

public class BuildDataset {

    private BuildDataset() {
    }

    private static final List<Dataset> dataset = new ArrayList<>();

    /**
     * Determina tutti i file per ogni ultimo commit di ogni release
     *
     * @param commits:     lista dei commit
     * @param halfRelease: metà delle release
     * @param releases:    lista delle release
     * @throws IOException:
     * @return: dataset
     */
    public static List<Dataset> buildDataset(List<Commit> commits, int halfRelease, List<Release> releases) throws IOException {
        Map<Release, List<Commit>> mapReleaseCommits = ReleaseCommits.mapReleaseCommits(commits, releases, halfRelease);
        List<File> files = new ArrayList<>();
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
                        files.add(file);

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

        findClassTouched(mapReleaseCommits);
        Metric.findFileCreation(dataset);

        WriteCSV.writeFile(files);
        return dataset;
    }

    /**
     * Controlla se ci sono delle differenze nell'albero del commit e inserisce le metriche di ogni file
     *
     * @param mapReleaseCommits: map con chiave-valore. key: release; value: list commit
     * @throws IOException:
     */
    private static void findClassTouched(Map<Release, List<Commit>> mapReleaseCommits) throws IOException {
        //diffFormatter trova le differenze da i tree di due commit (ovvero tra i file)
        DiffFormatter diffFormatter = new DiffFormatter(NullOutputStream.INSTANCE);
        diffFormatter.setRepository(GitHandler.repository(MilestoneOne.PATH_PROJ));

        mapReleaseCommits.forEach((key, value1) -> value1.forEach(commit -> {
            RevCommit revCommit = commit.getRevCommit();
            RevCommit previousCommit = revCommit.getParentCount() > 0 ? revCommit.getParent(0) : null;

            if (previousCommit != null) {
                //diffFormatter restituisce una lista dei path dei file che sono differenti tra i due commit
                try {
                    for (DiffEntry entry : diffFormatter.scan(previousCommit, revCommit)) {
                        Metric.setMetrics(dataset, commit, entry, diffFormatter, key.getNumVersion());
                    }
                } catch (IOException e) {
                    Logger.errorLog("Errore in DiffFormatter");
                }
            }
        }));
    }
}


