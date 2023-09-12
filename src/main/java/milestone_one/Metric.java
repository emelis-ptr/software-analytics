package milestone_one;

import entity.Commit;
import entity.Dataset;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.treewalk.TreeWalk;
import util.GitHandler;
import util.MyLogger;
import util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static util.Constants.JAVA_EXT;

public class Metric {

    private Metric() {
    }

    private static final Map<String, List<Date>> mapCreationDate = new HashMap<>();


    /**
     * Calcolo metriche
     *
     * @param dataset:       dataset
     * @param commit:        commit
     * @param entry:         DiffEmtry
     * @param diffFormatter: DiffFormatter
     * @param numRelease:    release corrente
     */
    public static void calculateMetrics(List<Dataset> dataset, Commit commit, DiffEntry entry, DiffFormatter diffFormatter, Integer numRelease, String proj) {
        dataset.stream().filter(d -> d.getRelease().getNumVersion().equals(numRelease)).forEach(rowDataset -> {
            if (rowDataset.getFile().getNameFile().equals(entry.getNewPath())) {
                determineModifiedFiles(rowDataset, entry);
                determineDefectiveClass(rowDataset, commit);
                calculateLocMetrics(entry, diffFormatter, rowDataset);
                calculateNumFix(commit, rowDataset);
                rowDataset.setNumR(rowDataset.getNumR() + 1);
                rowDataset.addAuthors(commit.getAuthor());
                findFileCreation(rowDataset, proj);
            }
        });
    }

    /**
     * Determiniamo le classi defective considerando la IV
     *
     * @param rowDataset: row del dataset
     * @param commit:     commit
     */
    private static void determineDefectiveClass(Dataset rowDataset, Commit commit) {
        if (commit.getTicket() != null) {
            // si considerano defective tutte le classi con almeno un AV
            if (commit.getTicket().getAffectedVersion() != null) {
                commit.getTicket().getAffectedVersion().forEach(av -> {
                    if (av.getNumVersion().equals(rowDataset.getRelease().getNumVersion())) {
                        rowDataset.setBuggy(true);
                    }
                });

            }
            // si considerano defective le classi che hanno FV!=IV e IV==Release
            if (!commit.getTicket().getFixedVersion().equals(commit.getTicket().getInjectedVersion())
                    && rowDataset.getRelease().getNumVersion().equals(commit.getTicket().getInjectedVersion().getNumVersion())) {
                rowDataset.setBuggy(true);
            }
        }
    }

    /**
     * Se ci sono delle differenze nell'albero del commit e se il file trovato in precedenza nel dataset
     * è uguale al fine in DiffEntry allora viene settato come true per definire che è buggy
     *
     * @param rowDataset: record del dataset
     * @param entry:      DiffEntry
     */
    public static void determineModifiedFiles(Dataset rowDataset, DiffEntry entry) {
        if ((entry.getChangeType().equals(DiffEntry.ChangeType.MODIFY) || entry.getChangeType().equals(DiffEntry.ChangeType.DELETE)) && entry.getNewPath().endsWith(JAVA_EXT)) {
            rowDataset.getFile().setModified(true);
        }
        if (entry.getChangeType().equals(DiffEntry.ChangeType.RENAME)) {
            rowDataset.getFile().setRenamed(true);
            rowDataset.getFile().setOldNameFile(entry.getOldPath());
        }
    }

    /**
     * Determina il numero di LOC (aggiunti, cancellati e modificati) toccati, media e valore massimo e
     * il numero di churn (aggiunti, cancellati)
     *
     * @param entry:         un valore che rappresenta un cambiamento al file tramite DiffEntry
     * @param diffFormatter: DiffFormatter
     * @param rowDataset:    row corrente del dataset
     */
    public static void calculateLocMetrics(DiffEntry entry, DiffFormatter diffFormatter, Dataset rowDataset) {
        int locTouched;
        int locAdded = 0;
        int locDeleted = 0;
        int locReplaced = 0;
        int locAddedOnce;
        int churnOnce;
        int churn;

        ArrayList<Integer> locAddedList = new ArrayList<>();
        ArrayList<Integer> churnList = new ArrayList<>();

        try {
            for (Edit edit : diffFormatter.toFileHeader(entry).toEditList()) {
                // LOC aggiunte
                if (edit.getType().equals(Edit.Type.INSERT)) {
                    locAddedOnce = edit.getEndB() - edit.getBeginB();
                    locAdded += edit.getEndB() - edit.getBeginB();
                    locAddedList.add(locAddedOnce);
                    // LOC cancellate
                } else if (edit.getType().equals(Edit.Type.DELETE)) {
                    locDeleted += edit.getEndA() - edit.getBeginA();
                    // LOC modificate
                } else if (edit.getType().equals(Edit.Type.REPLACE)) {
                    locReplaced += edit.getEndB() - edit.getBeginB();
                }
                churnOnce = locAdded - locDeleted;
                churnList.add(churnOnce);
            }
        } catch (IOException e) {
            MyLogger.errorLog("Errore calcolo LOC");
        }

        //LOC
        locAddedList.add(rowDataset.getLocAdded());
        locTouched = locAdded + locDeleted + locReplaced;

        rowDataset.setLocTouched(locTouched);
        rowDataset.setLocAdded(locAdded);
        rowDataset.setMaxLocAdded(Collections.max(locAddedList));
        rowDataset.setAvgLocAdded((float) locAddedList.stream().mapToInt(a -> a).summaryStatistics().getAverage());

        //CHURN
        churnList.add(rowDataset.getChurn());
        churn = locAdded - locDeleted;

        rowDataset.setChurn(churn);
        rowDataset.setMaxChurn(Collections.max(churnList));
        rowDataset.setAvgChurn((float) churnList.stream().mapToInt(a -> a).summaryStatistics().getAverage());
    }

    /**
     * Calcola la dimensione LOC di ogni file
     *
     * @param treeWalk: percorre uno o più AbstractTreeIterator in paralleloPercorre uno o più
     *                  AbstractTreeIterator in parallelo
     * @return : numero di LOC di un file
     */
    public static int calculateSize(TreeWalk treeWalk, String proj) {

        int lines = 0;
        try (Git git = GitHandler.git(proj)) {
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
            MyLogger.errorLog("Errore nel calcolo della dimensione LOC del file");
        }

        return lines;
    }

    /**
     * Calcola il numero di fix associato al file
     *
     * @param commit     : commit
     * @param rowDataset : record del dataset
     */
    public static void calculateNumFix(Commit commit, Dataset rowDataset) {
        if (commit.getTicket() != null) {
            rowDataset.setNumFix(rowDataset.getNumFix() + 1);
        }
    }

    /**
     * Trova la data di creazione del file
     *
     * @param rowDataset: record del dataset
     */
    private static void findFileCreation(Dataset rowDataset, String proj) {
        String nameFile = rowDataset.getFile().getNameFile();
        List<Date> commitsDate = new ArrayList<>();
        try {
            if (!mapCreationDate.containsKey(nameFile)) {
                GitHandler.git(proj).log()
                        .addPath(nameFile).call()
                        .forEach(revCommit -> commitsDate.add(revCommit.getCommitterIdent().getWhen()));
                mapCreationDate.put(nameFile, commitsDate);
            }
        } catch (GitAPIException | IOException e) {
            MyLogger.errorLog("Errore in Git per recuperare il primo commit del file");
        }

        mapCreationDate.forEach((key, value) -> value.sort(Comparator.comparing(Date::getTime)));

        if (mapCreationDate.containsKey(nameFile) && !mapCreationDate.get(nameFile).isEmpty()) {
            LocalDate dateCommit = Utils.convertToLocalDate(mapCreationDate.get(nameFile).get(0));
            rowDataset.getFile().setFileCreation(dateCommit);
        }
    }

}
