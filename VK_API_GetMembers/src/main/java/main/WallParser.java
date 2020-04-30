package main;

import com.google.gson.Gson;
import dataUser.User;
import dataBase.DataBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class WallParser {
    private static String token;
    private static final String versionAPI = "5.103";

    public static StringBuilder parserDataGroup(String response) throws IOException {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject responseObject = jsonObject.getJSONObject("response");
        JSONArray array = responseObject.getJSONArray("items");
        StringBuilder strId = new StringBuilder("");

        for (int i = 0; i < array.length(); i++) {
            strId.append(Integer.toString(array.getInt(i)));
            strId.append(",");
        }

        return strId;
    }

    public static ArrayList<User> parserDataUser(String response, ArrayList<User> arrayList) {
        DataBase dataBase = new DataBase();
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray array = jsonObject.getJSONArray("response");

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            User user = gson.fromJson(object.toString(), User.class);
            arrayList.add(user);
        }

        return arrayList;
    }

    public static Document connectDataUser(StringBuilder strId) throws IOException {
        String url = "https://api.vk.com/method/users.get";
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("name_case", "Nom");
        map.put("v", versionAPI);
        map.put("user_ids", strId.toString());
        map.put("fields", "city, country, home_town, " +
                "photo_max_orig, online, domain, has_mobile," +
                " contacts, site, Ceducation, Cstatus, Cconnections, " +
                "exports, activities");

        Document doc = Jsoup.connect("https://api.vk.com/method/users.get")
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .ignoreContentType(true)
                .data(map)
                .post();

        return doc;
    }

    public static Document connectDataGroup(String domain) throws IOException {
        String url = "https://api.vk.com/method/groups.getMembers";

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

        return doc;
    }

    public static void createDataBase(ArrayList<User> arrayList) throws SQLException, ClassNotFoundException {
        DataBase dataBase = new DataBase();
        dataBase.initDataBase(arrayList);
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        ArrayList<User> arrayList= new ArrayList<>();
        String domain = "iu7memes";
        StringBuilder strId;
        Document doc;

        System.out.print("Input token: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        token = reader.readLine();

        doc = connectDataGroup(domain);
        strId = parserDataGroup(doc.text());
        doc = connectDataUser(strId);

        parserDataUser(doc.text(), arrayList);
        createDataBase(arrayList);
    }
}
