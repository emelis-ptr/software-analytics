package milestone_one;

import entity.Dataset;
import entity.Release;
import entity.Ticket;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.NullOutputStream;
import util.LogFile;
import util.Repo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static util.Constants.JAVA_EXT;

public class RetrieveFile {

    private RetrieveFile() {
    }

    /**
     * Recupera tutti i file
     *
     * @param releases: lista delle release
     * @param tickets:  lista dei ticket
     * @return: lista dei file
     */
    public static List<Dataset> retrieveFiles(List<Release> releases, List<Ticket> tickets) {
        Map<Integer, List<RevCommit>> releaseCommits = ReleaseCommits.findReleaseCommits(releases, tickets);
        List<Dataset> datasets = new ArrayList<>();
        Dataset dataset;

        Path repoPath = Repo.returnPath(MilestoneOne.PATH_PROJ);

        try (Git git = Git.open(repoPath.toFile())) {
            Repository repository = Repo.repository(MilestoneOne.PATH_PROJ);

            TreeWalk treeWalk = new TreeWalk(git.getRepository());

            DiffFormatter diffFormatter = new DiffFormatter(NullOutputStream.INSTANCE);
            diffFormatter.setRepository(repository);

            List<RevCommit> lastCommit = new ArrayList<>();
            for (Map.Entry<Integer, List<RevCommit>> entry : releaseCommits.entrySet()) {

                RevCommit commit;
                // assegna l'ultimo commit della release se la lista dei commit non è vuota
                // altrimenti assegna il commit della release precedente
                if (!entry.getValue().isEmpty()) {
                    commit = entry.getValue().get(0);
                    lastCommit.add(commit);
                } else {
                    commit = lastCommit.get(lastCommit.size() - 1);
                }

                ObjectId treeId = commit.getTree();
                treeWalk.reset(treeId);
                treeWalk.setRecursive(true);

                while (treeWalk.next()) {
                    // considera solo i file che hanno l'estensione ".java"
                    if (treeWalk.getPathString().endsWith(JAVA_EXT)) {
                        String nameFile = treeWalk.getPathString();
                        dataset = new Dataset(releases.get(entry.getKey()), nameFile);

                        int size = calculateSize(treeWalk, git);
                        dataset.setSizeLoc(size);
                        //trova i file che sono stati toccati
                        findClassTouched(dataset, entry.getValue(), diffFormatter);

                        datasets.add(dataset);
                    }
                }
            }

        } catch (IOException e) {
            LogFile.errorLog("Errore nel recupero file");
        }

        // ordina il dataset in base al numero della release
        Collections.sort(datasets);
        return datasets;
    }

    /**
     * Calcola la dimensione LOC di ogni file
     *
     * @param treeWalk: percorre uno o più AbstractTreeIterator in paralleloPercorre uno o più
     *                  AbstractTreeIterator in parallelo
     * @param git:      un oggetto Git per il repository git esistente
     * @return : numero di LOC di un file
     */
    private static int calculateSize(TreeWalk treeWalk, Git git) {
        int lines = 0;
        try {
            ObjectLoader loader = git.getRepository().open(treeWalk.getObjectId(0));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            loader.copyTo(output);

            String contentFile = output.toString();
            // frammenta stringa in token quando trova \n
            StringTokenizer token = new StringTokenizer(contentFile, "\n");

            while (token.hasMoreTokens()) {
                lines++;
                token.nextToken();
            }
        } catch (IOException e) {
            LogFile.errorLog("Errore nel calcolo della dimensione LOC del file");
        }

        return lines;
    }

    /**
     * Controlla se ci sono delle differenze nell'albero del commit; se si allora quel file
     * è stato toccato e quindi vuol dire che è stato buggy
     *
     * @param dataset:       row corrente del dataset
     * @param commitsList:   lista dei commmit
     * @param diffFormatter: DiffFormatter
     * @throws IOException:
     */
    private static void findClassTouched(Dataset dataset, List<RevCommit> commitsList, DiffFormatter diffFormatter) throws IOException {
        for (RevCommit commit : commitsList) {
            //Commit precedente
            RevCommit previousCommit = commit.getParentCount() > 0 ? commit.getParent(0) : null;
            if (previousCommit != null) {
                //diffFormatter restituisce una lista dei path dei file che sono differenti tra i due commit
                for (DiffEntry entry : diffFormatter.scan(previousCommit, commit)) {
                    if ((entry.getChangeType().equals(DiffEntry.ChangeType.MODIFY) || entry.getChangeType().equals(DiffEntry.ChangeType.DELETE))
                            && entry.getNewPath().endsWith(JAVA_EXT)) {
                        setDefectsClasses(dataset, entry);
                    }
                }
            }
        }
    }

    /**
     * Se il file trovato in precedenza nel dataset è uguale al fine in DiffEntry
     * allora viene settato come true per definire che è buggy
     *
     * @param dataset: row corrente nel dataset
     * @param entry:   un valore che rappresenta un cambiamento al file tramite DiffEntry
     */
    private static void setDefectsClasses(Dataset dataset, DiffEntry entry) {
        if (dataset.getNameFile().equals(entry.getNewPath())) {
            dataset.setBuggy(true);
        }
    }


}
