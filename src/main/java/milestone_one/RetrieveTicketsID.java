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

import static util.Constants.*;


public class RetrieveTicketsID {

    /**
     * Metodo che prende da Jira i tickets e FV, OV, e AV se presenti
     *
     * @return :
     * @throws IOException:
     * @throws ParseException:
     */
    public static List<TicketJira> retrieveTicketJira() throws IOException, ParseException {
        List<Release> releases = RetrieveRelease.retrieveRelease();
        ArrayList<TicketJira> ticketJiras = new ArrayList<>();

        int j;
        int i = 0;
        int total;
        //Get JSON API for closed bugs w/ AV in the project
        do {
            //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
            j = i + 1000;
            String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                    + MilestoneOne.PROJ_NAME + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                    + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                    + i + "&maxResults=" + j;

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
                ticketJiras.add(new TicketJira(idTicket, createdDate, resolutionDate));
                setAV(ticketJiras.get(ticketJiras.size() - 1), listAV, releases);
            }
        } while (i < total);

        for (TicketJira version : ticketJiras) {
            setOV(version, version.getCreationDate(), releases);
        }
        return ticketJiras;
    }

    /**
     * Per ogni Ticket inserisce OV se la data di creazione del Ticket è uguale o avviene prima della
     * data di creazione della release
     *
     * @param ticketJira:  ticket presente su Jira
     * @param createdDate: data di creazione del ticket presente su Jira
     * @param releases:    lista delle release del progetto
     **/
    public static void setOV(TicketJira ticketJira, LocalDate createdDate, List<Release> releases) {
        for (Release release : releases) {
            if (createdDate.equals(release.getDateCreation()) || release.getDateCreation().isAfter(createdDate)) {
                ticketJira.setOpeningVersion(release);
                break;
            } else {
                ticketJira.setOpeningVersion(releases.get(releases.size() - 1));
            }
        }
    }

    /***
     * Per ogni Ticket inserisce FV se resolutionDate del ticket è uguale o avviene prima della data
     * di creazione della release
     *
     * @param ticketJira: ticket presente su Jira
     * @param releases: lista delle release del progetto
     * @param resolutionDate: data di fix del bug del ticket
     **/
    public static void setFV(TicketJira ticketJira, LocalDate resolutionDate, List<Release> releases) {
        for (Release release : releases) {
            if (resolutionDate.equals(release.getDateCreation()) || release.getDateCreation().isAfter(resolutionDate)) {
                ticketJira.setFixedVersion(release);
                break;
            }
        }
    }

    /**
     * Per ogni ticket se su Jira sono presenti più AV, li confronta con le realese e assegna quella corrispondente
     *
     * @param ticketJira: ticket presente su Jira
     * @param listAV:     lista delle affected version presenti su Jira
     * @param releases:   lista delle release del progetto
     */
    public static void setAV(TicketJira ticketJira, List<String> listAV, List<Release> releases) {
        List<Release> releaseAV = new ArrayList<>();
        if (!listAV.isEmpty()) {
            for (String av : listAV) {
                for (Release release : releases) {
                    if (release.getReleaseID().equals(av)) {
                        releaseAV.add(release);
                        ticketJira.setAffectedVersion(releaseAV);
                    }
                }
            }
        }
    }

}

