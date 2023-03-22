import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

    private JsonUtils() {
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream(); InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            BufferedReader rd = new BufferedReader(inputStreamReader);
            String jsonText = readAll(rd);

            return new JSONObject(jsonText);
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream(); InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            BufferedReader rd = new BufferedReader(inputStreamReader);
            String jsonText = readAll(rd);

            return new JSONArray(jsonText);
        }
    }
}