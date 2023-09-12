package util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static util.Constants.BRANCH;
import static util.Constants.PATH_REPOSITORY;
import static util.Utils.path;

public class GitHandler {

    private GitHandler() {
    }

    /**
     * Clone repository
     *
     * @param folder: cartella del repository in locale
     * @param proj:    nome del progetto GitHub
     */
    public static void cloneRemote(File folder, String proj) {
        String path = folder + File.separator + proj.toLowerCase();
        File dirProj = new File(path);
        if (!dirProj.exists()) {
            try {
                Git.cloneRepository()
                        .setURI("https://github.com/apache/" + proj.toLowerCase())
                        .setDirectory(dirProj)
                        .setBranch(BRANCH)
                        .call()
                        .close();
            } catch (GitAPIException e) {
                MyLogger.errorLog("Impossibile clonare il repository");
            }
            MyLogger.infoLog("Il repository clonato");
        } else {
            MyLogger.infoLog("Il repository in locale è stato già clonato");
        }
    }

    /**
     * Return string path
     *
     * @param path:
     * @return:
     */
    public static String returnRepo(String path) {
        //Prende il repository path definito all'interno del path_repository.properties
        Properties properties = new Properties();
        try (FileInputStream fileInput = new FileInputStream(PATH_REPOSITORY)) {
            properties.load(fileInput);
        } catch (Exception e) {
            MyLogger.errorLog("Errore nel file path repository.");
        }
        return properties.getProperty(path);
    }

    /**
     * Return repository
     *
     * @param path:
     * @throws IOException :
     * @return:
     */
    public static Repository repository(String path) throws IOException {
        String repoPath = returnRepo(path);

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        return repositoryBuilder.setGitDir(new File(repoPath)).setMustExist(true).build();
    }

    /**
     * Metodo che ritorna il Path per il treeWalk
     *
     * @return:
     */
    public static Path returnPath(String path) {
        String rootPath = returnRepo(path);
        return Paths.get(rootPath);
    }

    /**
     * Ritorna l'iterazione di git
     *
     * @throws IOException:
     * @throws GitAPIException:
     * @return:
     */
    public static Iterable<RevCommit> logsCommits(String proj) throws IOException, GitAPIException {
        Repository repository = GitHandler.repository(path(proj));
        Git git = new Git(repository);
        return git.log().call();
    }

    /**
     * Git
     *
     * @throws IOException:
     * @return: git
     */
    public static Git git(String proj) throws IOException {
        Path repoPath = GitHandler.returnPath(path(proj));
        return Git.open(repoPath.toFile());
    }

    /**
     * Tree walk
     *
     * @return: tree walk
     */
    public static TreeWalk treeWalk(String proj) {
        TreeWalk treeWalk = null;
        try (Git git = git(proj)) {
            treeWalk = new TreeWalk(git.getRepository());
        } catch (IOException e) {
            MyLogger.errorLog("Errore nel recupero file");
        }
        return treeWalk;
    }
}
