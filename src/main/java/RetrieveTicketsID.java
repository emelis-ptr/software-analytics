import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class RetrieveTicketsID {

    private RetrieveTicketsID() {
    }

    static String PROJ_NAME = "ACCUMULO";

    /**
     * Metodo che prende da Jira i tickets
     *
     * @throws IOException:
     */
    public static void main(String[] args) throws IOException {

        int j;
        int i = 0;
        int total;
        //Get JSON API for closed bugs w/ AV in the project
        do {
            //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
            j = i + 1000;
            String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                    + PROJ_NAME + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                    + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                    + i + "&maxResults=" + j;

            JSONObject json = JsonUtils.readJsonFromUrl(url);

            JSONArray issues = json.getJSONArray(Constants.ISSUES);
            total = json.getInt(Constants.TOTAL);

            for (; i < total && i < j; i++) {

                String idTicket = issues.getJSONObject(i % 1000).get(Constants.KEY).toString();
                JSONObject issue = issues.getJSONObject(i % 1000);

                System.out.println(idTicket);
                System.out.println(issue);
            }
        } while (i < total);
    }
}
