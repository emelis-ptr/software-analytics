package util;

import entity.Dataset;
import entity.Release;
import entity.TicketJira;
import milestone_one.MilestoneOne;

import java.io.FileWriter;
import java.util.List;

import static util.Constants.PATH_RESULTS;

public class WriteCSV {

    private WriteCSV() {
    }

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

    /**
     * Scrive in un file csv tutte le metriche del dataset
     *
     * @param datasets: metriche del dataset
     */
    public static void writeDataset(List<Dataset> datasets) {
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "-Dataset.csv";

        try (FileWriter fileWriter = new FileWriter(outname)) {
            fileWriter.append("Release, FileName, sizeLoc, locTouched, numR, numFix, numAuth, locAdded, avgLocAdded, maxLocAdded, churn, maxChurn, avgChurn, age, weightedAge, Bugginess");
            fileWriter.append("\n");

            for (Dataset metric : datasets) {
                fileWriter.append(metric.getRelease().getNumVersion().toString());
                fileWriter.append(",");
                fileWriter.append(metric.getNameFile());
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getSizeLoc()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getLocTouched()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getNumR()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getNumFix()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getNumAuth()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getLocAdded()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getAvgLocAdded()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getMaxLocAdded()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getChurn()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getMaxChurn()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getAvgChurn()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getAge()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(metric.getWeightedAge()));
                fileWriter.append(",");
                if (metric.isBuggy()) {
                    fileWriter.append("true");
                } else {
                    fileWriter.append("false");
                }
                fileWriter.append("\n");
            }
        } catch (Exception e) {
            LogFile.errorLog("Error in csv writer Dataset");
        }
    }

}
