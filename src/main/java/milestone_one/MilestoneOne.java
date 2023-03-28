package milestone_one;

import entity.Release;
import entity.TicketJira;
import util.LogFile;

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
            List<TicketJira> giraInfo = RetrieveTicketsID.retrieveTicketJira();

            LogFile.infoLog("*** Release *** \n" + "Release-size: " + releases.size() + "\n");
            System.out.println(releases);
            System.out.println(giraInfo);

        } catch (IOException e) {
            LogFile.errorLog("Exception MilestoneOne");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
