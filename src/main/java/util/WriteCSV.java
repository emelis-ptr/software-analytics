package util;

import entity.*;
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
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "/" + MilestoneOne.PROJ_NAME + "-Release.csv";

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
            Logger.errorLog("Error in csv writer ReleaseInfo");
        }
    }

    /**
     * Scrive in un file csv tutti i ticket con le versioni
     *
     * @param ticketJiras: lista dei ticket presenti su Jira
     */
    public static void writeTicketJira(List<TicketJira> ticketJiras) {
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "/" + MilestoneOne.PROJ_NAME + "-TicketJira.csv";

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
            Logger.errorLog("Error in csv writer TicketJira");
        }
    }

    /**
     * Scrive in un file csv tutte le metriche del dataset
     *
     * @param dataset: metriche del dataset
     */
    public static void writeDataset(List<Dataset> dataset) {
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "/" + MilestoneOne.PROJ_NAME + "-Dataset.csv";

        try (FileWriter fileWriter = new FileWriter(outname)) {
            fileWriter.append("Release, FileName, sizeLoc, locTouched, numR, numFix, numAuth, locAdded, avgLocAdded, maxLocAdded, churn, maxChurn, avgChurn, age, weightedAge, Bugginess");
            fileWriter.append("\n");

            for (Dataset rowDataset : dataset) {
                fileWriter.append(rowDataset.getRelease().getNumVersion().toString());
                fileWriter.append(",");
                fileWriter.append(rowDataset.getFile().getNameFile());
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getSizeLoc()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getLocTouched()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getNumR()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getNumFix()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getNumAuth()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getLocAdded()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getAvgLocAdded()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getMaxLocAdded()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getChurn()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getMaxChurn()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getAvgChurn()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getAge()));
                fileWriter.append(",");
                fileWriter.append(String.valueOf(rowDataset.getWeightedAge()));
                fileWriter.append(",");
                if (rowDataset.isBuggy()) {
                    fileWriter.append("true");
                } else {
                    fileWriter.append("false");
                }
                fileWriter.append("\n");
            }
        } catch (Exception e) {
            Logger.errorLog("Error in csv writer Dataset");
        }
    }

    /**
     * Scrive in un file csv tutte le metriche del dataset
     *
     * @param commits: lista dei commit
     */
    public static void writeCommit(List<Commit> commits) {
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "/" + MilestoneOne.PROJ_NAME + "-Commit.csv";

        try (FileWriter fileWriter = new FileWriter(outname)) {
            fileWriter.append("ShadID; Commit; Message; Ticket; Date; Authors; Version; Files");
            fileWriter.append("\n");

            for (Commit commit : commits) {
                fileWriter.append(commit.getShaId());
                fileWriter.append(";");

                fileWriter.append(String.valueOf(commit.getRevCommit()));
                fileWriter.append(";");
                fileWriter.append(commit.getMessage().replace(";", ","));
                fileWriter.append(";");
                if (commit.getTicket() != null) {
                    fileWriter.append(commit.getTicket().getIdTicket());
                } else {
                    fileWriter.append("");
                }
                fileWriter.append(";");
                fileWriter.append(commit.getDate().toString());
                fileWriter.append(";");
                fileWriter.append(commit.getAuthor());
                fileWriter.append(";");
                fileWriter.append(String.valueOf(commit.getRelease().getNumVersion()));
                fileWriter.append(";");
                if (!commit.getFiles().isEmpty()) {
                    fileWriter.append(commit.getNumFiles().toString());
                } else {
                    fileWriter.append("");
                }
                fileWriter.append("\n");
            }
        } catch (Exception e) {
            Logger.errorLog("Error in csv writer Commit");
        }
    }

    /**
     * Scrive in un file csv tutte le informazioni riguardo un ticket sul repository
     *
     * @param tickets: lista dei ticket
     */
    public static void writeTicket(List<Ticket> tickets) {
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "/" + MilestoneOne.PROJ_NAME + "-Ticket.csv";

        try (FileWriter fileWriter = new FileWriter(outname)) {
            fileWriter.append("TicketID; Commit; LastCommit; LastDateCommit");
            fileWriter.append("\n");

            for (Ticket ticket : tickets) {
                fileWriter.append(ticket.getTicketJira().getIdTicket());
                fileWriter.append(";");

                fileWriter.append(String.valueOf(ticket.getNumCommit()));
                fileWriter.append(";");
                fileWriter.append(ticket.getLastCommit().getRevCommit().toString());
                fileWriter.append(";");
                fileWriter.append(ticket.getLastDateCommit().toString());
                fileWriter.append("\n");
            }
        } catch (Exception e) {
            Logger.errorLog("Error in csv writer Ticket");
        }
    }

    /**
     * Scrive in un file csv tutte le informazioni riguardanti un file
     *
     * @param files: lista dei file
     */
    public static void writeFile(List<File> files) {
        String outname = PATH_RESULTS + MilestoneOne.PROJ_NAME + "/" + MilestoneOne.PROJ_NAME + "-Files.csv";

        try (FileWriter fileWriter = new FileWriter(outname)) {
            fileWriter.append("File name; Release; Commit; Old name file; SizeLOC; Age");
            fileWriter.append("\n");

            for (File file : files) {
                fileWriter.append(file.getNameFile());
                fileWriter.append(";");
                fileWriter.append(file.getRelease().getNumVersion().toString());
                fileWriter.append(";");
                fileWriter.append(file.getCommit().getRevCommit().toString());
                fileWriter.append(";");
                fileWriter.append(file.getOldNameFile());
                fileWriter.append(";");
                fileWriter.append(String.valueOf(file.getSizeLOC()));
                fileWriter.append(";");
                fileWriter.append(String.valueOf(file.getAge()));
                fileWriter.append("\n");
            }
        } catch (Exception e) {
            Logger.errorLog("Error in csv writer File");
        }
    }

}
