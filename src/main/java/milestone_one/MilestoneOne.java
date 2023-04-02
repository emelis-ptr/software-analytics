package milestone_one;

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
            List<Ticket> ticket = RetrieveTicketGit.retrieveTicketGit(ticketJiras);

            Proportion.proportionMethod(releases, ticketJiras, ticket);

            LogFile.infoLog("*** Release *** \n" + "Release-size: " + releases.size() + "\n");

            WriteCSV.writeTicketJira(ticketJiras);
        } catch (IOException | ParseException | GitAPIException e) {
            LogFile.errorLog("Exception MilestoneOne");
        }

    }

}
