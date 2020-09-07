package com.vk_api_lib.dataUsers.wallParser;

import com.google.gson.Gson;
import com.vk_api_lib.dataUsers.dataUser.User;
import com.vk_api_lib.dataUsers.dataBase.DataBase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class WallParser {
    private static String token;
    private static String versionAPI;
    private static String domain;
    private static DataBase dataBase;
    private static int countVKGroup;

    public WallParser(String token, String versionAPI, String domain, DataBase dataBase) {
        WallParser.token = token;
        WallParser.versionAPI = versionAPI;
        WallParser.domain = domain;
        WallParser.dataBase = dataBase;
    }

    private static int fiendCountVKGroup(String response) {
        return (new JSONObject(response)).getJSONObject("response").getInt("count");
    }

    private static StringBuilder parserDataGroup(String response) {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject responseObject = jsonObject.getJSONObject("response");
        JSONArray array = responseObject.getJSONArray("items");
        StringBuilder strId = new StringBuilder();

        for (int i = 0; i < array.length(); i++) {
            strId.append(Integer.toString(array.getInt(i)));
            strId.append(",");
        }

        return strId;
    }

    private static void parserDataUser(String response, ArrayList<User> arrayList) {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(response);
        System.out.println(jsonObject);
        JSONArray array = jsonObject.getJSONArray("response");

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            User user = gson.fromJson(object.toString(), User.class);
            arrayList.add(user);
        }

    }

    private static Document connectDataUser(StringBuilder strId) throws IOException {
        String url = "https://api.vk.com/method/users.get";
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("name_case", "Nom");
        map.put("v", versionAPI);
        map.put("user_ids", strId.toString());
        map.put("fields", "city, country, home_town, " +
                "photo_max_orig, online, domain, has_mobile," +
                " contacts, site, education, connections, " +
                "exports, activities");

        return Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .ignoreContentType(true)
                .data(map)
                .post();
    }

    private static Document connectDataGroup(String domain, String offset) throws IOException {
        String url = "https://api.vk.com/method/groups.getMembers";

        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("offset", offset); //смещение
        map.put("group_id", domain);
        map.put("count", "1000");
        map.put("v", versionAPI);

        return Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .ignoreContentType(true)
                .data(map)
                .post();
    }

    private static void createDataBase(ArrayList<User> arrayList) {
        try {
            dataBase.initDataBase(arrayList);
        }
        catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void getUsers(String offset) {
        try {
            ArrayList<User> arrayList = new ArrayList<>();
            StringBuilder strId;
            Document doc;

            for (int offsetNow = 0; offsetNow <= countVKGroup; offsetNow += 1000) {
                doc = connectDataGroup(domain, String.valueOf(offsetNow));
                countVKGroup = fiendCountVKGroup(doc.text());
                strId = parserDataGroup(doc.text());
                doc = connectDataUser(strId);
                parserDataUser(doc.text(), arrayList);
                System.out.println(arrayList.size());
                createDataBase(arrayList);
                arrayList.clear();
                System.out.println(offsetNow);
                //Thread.sleep(0,33);
            }

        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String token = "974e814f974e814f974e814f68973f0f159974e974e814fc9e4672f03f17c57623e9aef";
        String versionAPI = "5.103";
        String domain = "msu_official";

        DataBase dataBase = new DataBase("root", "root", "jdbc:mysql://localhost:3306/test?useSSL=false");
        WallParser wallParser = new WallParser(token, versionAPI, domain, dataBase);

        wallParser.getUsers(String.valueOf(countVKGroup));
    }
}