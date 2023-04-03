package milestone_one;

import entity.Dataset;
import entity.Release;
import entity.Ticket;
import entity.TicketJira;
import org.eclipse.jgit.api.errors.GitAPIException;
import util.LogFile;
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
        LogFile.setupLogger();

        try {
            List<Release> releases = RetrieveRelease.retrieveRelease();
            List<TicketJira> ticketJiras = RetrieveTicketsJira.retrieveTicketJira();
            List<Ticket> tickets = RetrieveTicketGit.retrieveTicketGit(ticketJiras);

            Proportion.proportionMethod(releases, ticketJiras, tickets);

            List<Dataset> datasets = RetrieveFile.retrieveFiles(releases, tickets);
            System.out.println(datasets);

            LogFile.infoLog("*** Release *** \n" + "Numero di release: " + releases.size() + "\n");
            LogFile.infoLog("*** Ticket *** \n" + "Numero di ticket: " + tickets.size() + "\n");
            LogFile.infoLog("*** Dataset *** \n" + "Dimensione del dataset: " + datasets.size() + "\n");

            WriteCSV.writeTicketJira(ticketJiras);
            WriteCSV.writeDataset(datasets);
        } catch (IOException | ParseException |
                 GitAPIException e) {
            LogFile.errorLog("Exception MilestoneOne");
        }
    }
}
