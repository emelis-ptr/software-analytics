package util;

import milestone_one.MilestoneOne;
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

import static util.Constants.PATH_REPOSITORY;

public class GitHandler {

    private GitHandler() {
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
            Logger.errorLog("Errore nel file path repository.");
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
    public static Iterable<RevCommit> logsCommits() throws IOException, GitAPIException {
        Repository repository = GitHandler.repository(MilestoneOne.path());
        Git git = new Git(repository);
        return git.log().call();
    }

    /**
     * Git
     *
     * @throws IOException:
     * @return: git
     */
    public static Git git() throws IOException {
        Path repoPath = GitHandler.returnPath(MilestoneOne.path());
        return Git.open(repoPath.toFile());
    }

    /**
     * Tree walk
     *
     * @return: tree walk
     */
    public static TreeWalk treeWalk() {
        TreeWalk treeWalk = null;
        try (Git git = git()) {
            treeWalk = new TreeWalk(git.getRepository());
        } catch (IOException e) {
            Logger.errorLog("Errore nel recupero file");
        }
        return treeWalk;
    }
}
