package util;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

import static util.Constants.PATH_REPOSITORY;

public class Repo {

    private Repo() {
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
        try(FileInputStream fileInput = new FileInputStream(PATH_REPOSITORY)) {
            properties.load(fileInput);
        } catch(Exception e) {
            LogFile.errorLog("Errore nel file path repository.");}
        return properties.getProperty(path);
    }

    /**
     * Return repository
     *
     * @param path:
     * @return:
     * @throws IOException :
     */
    public static Repository repository(String path) throws IOException {
        String repoPath = returnRepo(path);

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        return repositoryBuilder.setGitDir(new File(repoPath)).setMustExist(true).build();
    }

    /**Metodo che ritorna il Path per il treeWalk
     *
     * @return:
     */
    public static Path returnPath(String path) {
        String rootPath = returnRepo(path);
        return Paths.get(rootPath);
    }

    /**
     * Metodo che ritorna l'iterazione di git
     *
     * @return:
     * @throws IOException:
     * @throws GitAPIException:
     */
    public static Iterator<RevCommit> iteratorGit(String path) throws IOException, GitAPIException {
        Git git = new Git(repository(path));
        Iterable<RevCommit> log = git.log().all().call();
        return log.iterator();
    }
}
