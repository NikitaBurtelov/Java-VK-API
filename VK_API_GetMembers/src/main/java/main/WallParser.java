package main;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class WallParser {
    public static JSONArray getMembersVK(String response) {
        JSONObject userJson = new JSONObject(response);
        JSONObject responseObject = userJson.getJSONObject("response");
        JSONArray array = responseObject.getJSONArray("items");

        for (int i = 0; i < array.length(); i++) {
            int val = array.getInt(i);
            System.out.println(val);
        }

        return array;
    }

    public static void main(String[] args) throws IOException {
        String token = "";
        String versionAPI = "5.103";
        String url = "https://api.vk.com/method/groups.getMembers";
        String domain = "iu7memes";

        System.out.print("Input token: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        token = reader.readLine();

        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("group_id", domain);
        map.put("v", versionAPI);

        Document doc = Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .ignoreContentType(true)
                .data(map)
                .post();

        getMembersVK(doc.text());
    }
}
