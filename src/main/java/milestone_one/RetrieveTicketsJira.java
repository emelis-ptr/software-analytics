package milestone_one;

import entity.Release;
import entity.TicketJira;
import org.json.JSONArray;
import org.json.JSONObject;
import util.JsonUtils;
import util.Utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static milestone_one.MilestoneOne.PROJ_NAME_M1;
import static util.Constants.*;


public class RetrieveTicketsJira {

    private RetrieveTicketsJira() {
    }

    /**
     * Recupera da Jira i tickets e FV, OV, e AV se presenti
     *
     * @param releases: lista delle release
     * @return : lista dei ticket presenti su Jira
     * @throws IOException:
     * @throws ParseException:
     */
    public static List<TicketJira> retrieveTicketJira(List<Release> releases) throws IOException, ParseException {
        ArrayList<TicketJira> ticketJiras = new ArrayList<>();
        TicketJira ticketJira;

        int j;
        int i = 0;
        int total;
        //Get JSON API for closed bugs w/ AV in the project
        do {
            //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
            j = i + 1000;
            String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22" + MilestoneOne.project(PROJ_NAME_M1) + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR" + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt=" + i + "&maxResults=" + j;

            JSONObject json = JsonUtils.readJsonFromUrl(url);

            JSONArray issues = json.getJSONArray(ISSUES);
            total = json.getInt(TOTAL);

            int totalVersion;
            String nameVersion;

            for (; i < total && i < j; i++) {
                ArrayList<String> listAV = new ArrayList<>();

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");

                String idTicket = issues.getJSONObject(i % 1000).get(KEY).toString();
                JSONObject issue = issues.getJSONObject(i % 1000);

                LocalDate createdDate = Utils.convertToLocalDate(df.parse(issue.getJSONObject(FIELDS).get(CREATED).toString()));
                LocalDate resolutionDate = Utils.convertToLocalDate(df.parse(issue.getJSONObject(FIELDS).get(RESOLUTION_DATE).toString()));

                JSONArray affectedVersions = issue.getJSONObject(FIELDS).getJSONArray(VERSIONS);
                totalVersion = affectedVersions.length();

                for (int k = 0; k < totalVersion; k++) {
                    nameVersion = affectedVersions.getJSONObject(k).get(ID_VERSION).toString();
                    listAV.add(nameVersion);
                }
                ticketJira = new TicketJira(idTicket, createdDate, resolutionDate);

                // inserisco le versioni di ogni ticket
                setOV(ticketJira, releases);
                setAV(ticketJira, listAV, releases);
                setIV(ticketJira);

                ticketJiras.add(ticketJira);
            }
        } while (i < total);

        return ticketJiras;
    }

    /**
     * Per ogni Ticket inserisce OV se la data di creazione del Ticket è uguale o avviene prima della
     * data di creazione della release
     *
     * @param ticketJira: ticket presente su Jira
     * @param releases:   lista delle release del progetto
     **/
    private static void setOV(TicketJira ticketJira, List<Release> releases) {
        for (Release release : releases) {
            if (ticketJira.getCreationDate().equals(release.getDateCreation()) || release.getDateCreation().isAfter(ticketJira.getCreationDate())) {
                ticketJira.setOpeningVersion(release);
                break;
            }
        }
    }

    /**
     * Per ogni ticket se su Jira sono presenti più AV, li confronta con le realese e assegna
     * quella corrispondente
     *
     * @param ticketJira: ticket presente su Jira
     * @param listAV:     lista delle affected version presenti su Jira
     * @param releases:   lista delle release del progetto
     */
    private static void setAV(TicketJira ticketJira, List<String> listAV, List<Release> releases) {
        if (!listAV.isEmpty()) {
            //Inserisco le affected versions
            listAV.forEach(av -> releases.stream()
                    .filter(release -> release.getReleaseID().equals(av))
                    .forEach(ticketJira::addAffectedVersion));
        }
    }

    /**
     * Per ogni ticket verifichiamo che AV sia disponibile e consistente e la consideriamo come IV
     *
     * @param ticketJira: ticket presente su Jira
     */
    private static void setIV(TicketJira ticketJira) {
        // verifichiamo che AV sia disponibile
        if (!ticketJira.getAffectedVersion().isEmpty()
                && ticketJira.getOpeningVersion() != null
                // verifichiamo che AV sia consistente se AV <= OV
                && (ticketJira.getAffectedVersion().get(0).getNumVersion() <= ticketJira.getOpeningVersion().getNumVersion())) {
            ticketJira.setInjectedVersion(ticketJira.getAffectedVersion().get(0));
        }
    }

}

