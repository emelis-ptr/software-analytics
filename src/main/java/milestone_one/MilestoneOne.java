package milestone_one;

import entity.*;
import enums.Projects;
import org.eclipse.jgit.api.errors.GitAPIException;
import util.GitHandler;
import util.MyLogger;
import util.WriteCSV;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static milestone_one.Proportion.proportions;
import static util.Utils.path;
import static util.Utils.project;

public class MilestoneOne {
    public static final String PROJ_NAME_M1 = project(String.valueOf(Projects.Project.BOOKKEEPER));

    public static void main(String[] args) {
        MyLogger.setupLogger();
        MyLogger.infoLog(" --> " + PROJ_NAME_M1);
        GitHandler.cloneRemote(GitHandler.returnPath(path("")).toFile(), PROJ_NAME_M1);

        try {
            MyLogger.infoLog(" --> Determiniamo le release");
            List<Release> releases = RetrieveRelease.retrieveRelease(PROJ_NAME_M1);
            WriteCSV.writeRelease(releases);
            MyLogger.infoLog("*** Release *** \n" + "Numero di release: " + releases.size() + "\n");
            // consideriamo solo metà delle release
            int halfRelease = (releases.size() / 2);

            MyLogger.infoLog(" --> Determiniamo i ticket presenti su Jira");
            List<TicketJira> ticketJiras = RetrieveTicketsJira.retrieveTicketJira(releases, PROJ_NAME_M1);

            MyLogger.infoLog(" --> Determiniamo i commit del progetto");
            List<Commit> commits = RetrieveTicketGit.getCommits(ticketJiras, releases, PROJ_NAME_M1);

            MyLogger.infoLog(" --> Determiniamo i ticket che sono menzionati nel commit del progetto");
            long ticketOnGit = ticketJiras.stream().filter(Ticket::isContainedInACommit).count();
            MyLogger.infoLog("*** Ticket *** \n" + "Numero di ticket: " + ticketOnGit + "\n");
            MyLogger.infoLog("Numero di Ticket non presenti su Git: " + (ticketJiras.size() - ticketOnGit) + "\n");

            MyLogger.infoLog(" --> Determiniamo IV attraverso il metodo proportion");
            double pColdStart = Proportion.calculateColdStart();
            Proportion.proportion(ticketJiras, releases, proportions, pColdStart);

            MyLogger.infoLog(" --> Recuperiamo i file");
            Map<Release, List<Commit>> mapReleaseCommits = ReleaseCommits.mapReleaseCommits(commits, halfRelease);
            List<Dataset> dataset = BuildDataset.buildDataset(mapReleaseCommits, PROJ_NAME_M1);
            MyLogger.infoLog("*** Dataset *** \n" + "Dimensione del dataset: " + dataset.size() + "\n");

            MyLogger.infoLog(" --> Si aggiungono le release mancanti al dataset");
            BuildDataset.addFilesToEmptyRelease(mapReleaseCommits, releases, dataset, halfRelease);

            MyLogger.infoLog(" --> Calcolo delle metriche");
            BuildDataset.findClassTouched(dataset, mapReleaseCommits, PROJ_NAME_M1);

            MyLogger.infoLog(" --> Stampiamo su un file csv");
            WriteCSV.writeDataset(dataset);
            WriteCSV.writeTicketJira(ticketJiras);
        } catch (IOException | ParseException | GitAPIException e) {
            MyLogger.errorLog("Exception MilestoneOne");
        }
    }



}
