package util;

import entity.Release;
import entity.TicketJira;
import milestone_one.MilestoneOne;

import java.io.FileWriter;
import java.util.List;

import static util.Constants.PATH_RESULTS;

public class WriteCSV {


    /**
     * Scrive in un file csv tutte le release
     *
     * @param releases: lista delle release
     */
    public static void writeRelease(List<Release> releases) {
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "-Release.csv";

        try (FileWriter fileWriter = new FileWriter(outname)) {
            fileWriter.append("Index;Version ID;Version Name;Date Creation");
            fileWriter.append("\n");

            for (Release release : releases) {
                fileWriter.append(release.getNumVersion().toString());
                fileWriter.append(";");
                fileWriter.append(release.getReleaseID());
                fileWriter.append(";");
                fileWriter.append(release.getReleaseName());
                fileWriter.append(";");
                fileWriter.append(release.getDateCreation().toString());
                fileWriter.append("\n");
            }
        } catch (Exception e) {
            LogFile.errorLog("Error in csv writer ReleaseInfo");
        }
    }

    /**
     * Scrive in un file csv tutti i ticket con le versioni
     *
     * @param ticketJiras: lista dei ticket presenti su Jira
     */
    public static void writeTicketJira(List<TicketJira> ticketJiras) {
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "-TicketJira.csv";

        try (FileWriter fileWriter = new FileWriter(outname)) {
            fileWriter.append("Tickets ID; Date Creation; Resolution Date; Injected Version; Opening Version; Fixed Version; Affected Version");
            fileWriter.append("\n");

            for (TicketJira ticketJira : ticketJiras) {
                fileWriter.append(ticketJira.getIdTicket());
                fileWriter.append(";");
                fileWriter.append(ticketJira.getCreationDate().toString());
                fileWriter.append(";");
                fileWriter.append(ticketJira.getResolutionDate().toString());
                fileWriter.append(";");
                fileWriter.append(ticketJira.getInjectedVersion().getNumVersion().toString());
                fileWriter.append(";");
                fileWriter.append(ticketJira.getOpeningVersion().getNumVersion().toString());
                fileWriter.append(";");
                fileWriter.append(ticketJira.getFixedVersion().getNumVersion().toString());
                fileWriter.append(";");

                if (ticketJira.getAffectedVersion() != null) {
                    for (Release av : ticketJira.getAffectedVersion()) {
                        fileWriter.append(av.getNumVersion().toString()).append(" - ");
                    }
                }
                fileWriter.append("\n");
            }
        } catch (Exception e) {
            LogFile.errorLog("Error in csv writer TicketJira");
        }
    }

}
