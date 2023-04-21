package milestone_one;

import entity.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import util.Logger;
import util.WriteCSV;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class MilestoneOne {

    protected static final String[] PROJS_NAME = {"Bookkeeper", "Syncope"};
    protected static final String[] PATHS = {"PATH_BOOKKEEPER", "PATH_SYNCOPE"};

    //bookkeeper = 0; syncope = 1
    public static final String PROJ_NAME = PROJS_NAME[0];
    public static final String PATH_PROJ = PATHS[0];

    public static void main(String[] args) {
        Logger.setupLogger();

        try {
            Logger.infoLog(" --> Determiniamo le release");
            List<Release> releases = RetrieveRelease.retrieveRelease();
            // consideriamo solo metà delle release
            int halfRelease = (releases.size() / 2);

            Logger.infoLog(" --> Determiniamo i ticket presenti su Jira");
            List<TicketJira> ticketJiras = RetrieveTicketsJira.retrieveTicketJira();

            Logger.infoLog(" --> Determiniamo i commit del progetto");
            List<Commit> commits = RetrieveTicketGit.getCommits(ticketJiras, releases);

            Logger.infoLog(" --> Determiniamo i ticket che sono menzionati nel commit del progetto");
            List<Ticket> tickets = RetrieveTicketGit.retrieveTicketGit(commits);

            Logger.infoLog(" --> Determiniamo IV attraverso il metodo proportion");
            Proportion.proportion(ticketJiras, tickets, releases);

            Logger.infoLog(" --> Costruiamo il dataset");
            List<Dataset> datasets = BuildDataset.buildDataset(commits, halfRelease, releases);

            Logger.infoLog("*** Release *** \n" + "Numero di release: " + releases.size() + "\n");
            Logger.infoLog("*** Ticket *** \n" + "Numero di ticket: " + tickets.size() + "\n");
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

}
