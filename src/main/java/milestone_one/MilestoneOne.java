package milestone_one;

import entity.*;
import enums.Path;
import enums.Project;
import org.eclipse.jgit.api.errors.GitAPIException;
import util.Logger;
import util.WriteCSV;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class MilestoneOne {
    public static final String PROJ_NAME1 = String.valueOf(Project.BOOKKEEPER);

    public static void main(String[] args) {
        Logger.setupLogger();
        Logger.infoLog(" --> " + project(PROJ_NAME1));
        try {
            Logger.infoLog(" --> Determiniamo le release");
            List<Release> releases = RetrieveRelease.retrieveRelease();
            Logger.infoLog("*** Release *** \n" + "Numero di release: " + releases.size() + "\n");
            // consideriamo solo metà delle release
            int halfRelease = (releases.size() / 2);

            Logger.infoLog(" --> Determiniamo i ticket presenti su Jira");
            List<TicketJira> ticketJiras = RetrieveTicketsJira.retrieveTicketJira();

            Logger.infoLog(" --> Determiniamo i commit del progetto");
            List<Commit> commits = RetrieveTicketGit.getCommits(ticketJiras, releases);

            Logger.infoLog(" --> Determiniamo i ticket che sono menzionati nel commit del progetto");
            List<Ticket> tickets = RetrieveTicketGit.retrieveTicketGit(commits);
            Logger.infoLog("*** Ticket *** \n" + "Numero di ticket: " + tickets.size() + "\n");

            Logger.infoLog(" --> Determiniamo IV attraverso il metodo proportion");
            Proportion.proportion(ticketJiras, tickets, releases);

            Logger.infoLog(" --> Costruiamo il dataset");
            List<Dataset> datasets = BuildDataset.buildDataset(commits, halfRelease, releases);
            Logger.infoLog("*** Dataset *** \n" + "Dimensione del dataset: " + datasets.size() + "\n");

            Logger.infoLog(" --> Stampiamo su un file csv");
            WriteCSV.writeDataset(datasets);
            WriteCSV.writeCommit(commits);
            WriteCSV.writeTicketJira(ticketJiras);
            WriteCSV.writeTicket(tickets);
        } catch (IOException | ParseException | GitAPIException e) {
            Logger.errorLog("Exception MilestoneOne");
        }
    }

    /**
     * @param projName: nome progetto
     * @return: stringa del progetto
     */
    public static String project(String projName) {
        return String.valueOf(projName).substring(0, 1).toUpperCase() + String.valueOf(projName).substring(1).toLowerCase();
    }

    /**
     * @return: path corrispondente al progetto
     */
    public static String path() {
        return switch (project(PROJ_NAME1)) {
            case "Bookkeeper" -> String.valueOf(Path.PATH_BOOKKEEPER);
            case "Syncope" -> String.valueOf(Path.PATH_SYNCOPE);
            default -> "";
        };
    }

}
