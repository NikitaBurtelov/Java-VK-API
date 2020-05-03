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


    public WallParser(String token, String versionAPI, String domain, DataBase dataBase) {
        WallParser.token = token;
        WallParser.versionAPI = versionAPI;
        WallParser.domain = domain;
        WallParser.dataBase = dataBase;
    }

    private static StringBuilder parserDataGroup(String response){
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
                " contacts, site, education, status, connections, " +
                "exports, activities");

        return Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .ignoreContentType(true)
                .data(map)
                .post();
    }

    private static Document connectDataGroup(String domain) throws IOException {
        String url = "https://api.vk.com/method/groups.getMembers";

        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", token);
        map.put("group_id", domain);
        map.put("v", versionAPI);

        return Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://www.google.com")
                .ignoreContentType(true)
                .data(map)
                .post();
    }

    private static void createDataBase(ArrayList<User> arrayList) throws SQLException, ClassNotFoundException {
        dataBase.initDataBase(arrayList);
    }

    public void getUsers() throws IOException, SQLException, ClassNotFoundException {
        ArrayList<User> arrayList= new ArrayList<>();
        StringBuilder strId;
        Document doc;

        //System.out.print("Input token: ");
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //token = reader.readLine();

        doc = connectDataGroup(domain);
        strId = parserDataGroup(doc.text());
        doc = connectDataUser(strId);

        parserDataUser(doc.text(), arrayList);
        createDataBase(arrayList);
    }
}

/*
String userName = "root";
String password = "root";
String connectionUrl = "jdbc:mysql://localhost:3306/test?useSSL=false";
*/