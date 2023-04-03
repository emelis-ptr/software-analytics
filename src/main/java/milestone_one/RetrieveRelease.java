package milestone_one;

import entity.Release;
import org.json.JSONArray;
import org.json.JSONObject;
import util.JsonUtils;
import util.WriteCSV;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static util.Constants.*;

public class RetrieveRelease {

    private RetrieveRelease() {
    }

    /**
     * Da Jira prende tutte le release del progetto
     *
     * @return : lista delle release
     * @throws IOException:
     */
    public static List<Release> retrieveRelease() throws IOException {
        ArrayList<Release> releases = new ArrayList<>();
        //Fills the arraylist with releases dates and orders them
        //Ignores releases with missing dates
        String url = "https://issues.apache.org/jira/rest/api/2/project/" + MilestoneOne.PROJ_NAME.toUpperCase(Locale.ROOT);
        JSONObject json;

        json = JsonUtils.readJsonFromUrl(url);
        JSONArray versions = json.getJSONArray(VERSIONS);

        for (int i = 0; i < versions.length(); i++) {
            String name = "";
            String id = "";
            if (versions.getJSONObject(i).has(RELEASE_DATE)) {
                if (versions.getJSONObject(i).has(NAME_VERSION))
                    name = versions.getJSONObject(i).get(NAME_VERSION).toString();
                if (versions.getJSONObject(i).has(ID_VERSION))
                    id = versions.getJSONObject(i).get(ID_VERSION).toString();
                String dateString = versions.getJSONObject(i).get(RELEASE_DATE).toString();
                LocalDate date = LocalDate.parse(dateString);
                releases.add(new Release(id, name, date));
            }
        }
        // order releases by date
        releases.sort(Comparator.comparing(Release::getDateCreation));
        setNumRelease(releases);

        WriteCSV.writeRelease(releases);
        return releases;
    }

    /**
     * Determina l'indice della release
     *
     * @param listRelease: lista delle release del progetto
     */
    private static void setNumRelease(List<Release> listRelease) {
        for (int i = 0; i < listRelease.size(); i++) {
            Integer index = i + 1;
            listRelease.get(i).setNumVersion(index);
        }
    }

}
