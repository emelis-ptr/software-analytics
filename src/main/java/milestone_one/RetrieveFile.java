package milestone_one;

import entity.Dataset;
import entity.Release;
import entity.Ticket;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
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
     * @throws JGitInternalException:
     * @throws IOException:
     * @return: lista dei file
     */
    public static List<Dataset> retrieveFiles(List<Release> releases, List<Ticket> tickets) throws IOException {
        Map<Integer, List<RevCommit>> releaseCommits = ReleaseCommits.findReleaseCommits(releases, tickets);
        List<Dataset> datasets = new ArrayList<>();
        Dataset dataset;

        Path repoPath = Repo.returnPath(MilestoneOne.PATH_PROJ);
        Git git = Git.open(repoPath.toFile());
        TreeWalk treeWalk = new TreeWalk(git.getRepository());

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
                    datasets.add(dataset);
                }
            }
        }

        git.close();
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
     * @throws IOException:
     */
    private static int calculateSize(TreeWalk treeWalk, Git git) throws IOException {
        ObjectLoader loader = git.getRepository().open(treeWalk.getObjectId(0));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        loader.copyTo(output);

        String contentFile = output.toString();
        // frammenta stringa in token quando trova \n
        StringTokenizer token = new StringTokenizer(contentFile, "\n");

        int lines = 0;
        while (token.hasMoreTokens()) {
            lines++;
            token.nextToken();
        }
        return lines;
    }


}
