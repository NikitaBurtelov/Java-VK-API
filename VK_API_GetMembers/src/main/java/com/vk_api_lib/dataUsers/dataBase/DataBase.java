package com.vk_api_lib.dataUsers.dataBase;

import com.vk_api_lib.dataUsers.dataUser.User;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    String userName;
    String password;
    String connectionUrl;

    public DataBase(String userName, String password, String connectionUrl) {
        this.userName = userName;
        this.password = password;
        this.connectionUrl = connectionUrl;
    }

    public void initDataBase(ArrayList<User> arrayList) throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
             /*statement.executeUpdate("drop table if exists Users");
             statement.executeUpdate("create table if not exists Users (id MEDIUMINT NOT NULL AUTO_INCREMENT, " +
                    "userId INTEGER, first_name CHAR(255), last_name CHAR (255), " +
                    "mobile_phone CHAR(50) NULL, connections VARCHAR(255), " +
                    "home_town CHAR(255), photo_max_orig VARCHAR(255), " +
                    "has_mobile INTEGER, activities VARCHAR(1000) NULL," +
                    " userOnline INTEGER, university_name CHAR(255), faculty_name CHAR(255)," +
                     "PRIMARY KEY (id))");
              */
            //statement.executeUpdate("Insert into Users");

            addDataBase(arrayList, connection);
            statement.close();
        }
    }

    private void addDataBase(ArrayList<User> arrayList, Connection connection) throws SQLException {
        String sql = "INSERT INTO Users (userId, first_name, last_name, mobile_phone," +
                " connections, home_town, photo_max_orig," +
                " has_mobile, activities, userOnline, university_name, faculty_name) Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        for (User user : arrayList) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.id);
            preparedStatement.setString(2, user.first_name);
            preparedStatement.setString(3, user.last_name);
            preparedStatement.setString(4, user.mobile_phone);
            preparedStatement.setString(5, user.connections);
            preparedStatement.setString(6, user.home_town);
            preparedStatement.setString(7, user.photo_max_orig);
            preparedStatement.setInt(8, user.has_mobile);
            preparedStatement.setString(9, user.activities);
            preparedStatement.setInt(10, user.online);
            preparedStatement.setString(11, user.university_name);
            preparedStatement.setString(12, user.faculty_name);

            //System.out.println(user.count);

            preparedStatement.executeUpdate();
        }
    }
}
