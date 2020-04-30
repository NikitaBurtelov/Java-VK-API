package dataBase;

import dataUser.User;
import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    public void initDataBase(ArrayList<User> arrayList) throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        String userName = "root";
        String password = "root";
        String connectionUrl = "jdbc:mysql://localhost:3306/test?useSSL=false";

        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("drop table Users");
            statement.executeUpdate("create table if not exists Users (id MEDIUMINT NOT NULL AUTO_INCREMENT, " +
                    "userId INTEGER, first_name CHAR(100), last_name CHAR (100), " +
                    "mobile_phone CHAR(50) NULL, home_town CHAR(255), photo_max_orig CHAR (255), " +
                    "has_mobile INTEGER, activities VARCHAR(1000) NULL, online INTEGER, PRIMARY KEY (id))");

            addDataBase(arrayList, connection);
        }
    }

    public void addDataBase(ArrayList<User> arrayList, Connection connection) throws SQLException {
        String sql = "INSERT INTO Users (userId, first_name, last_name, mobile_phone, home_town, photo_max_orig, has_mobile, activities, online) Values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println(arrayList.size());

        for (User user : arrayList) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.id);
            preparedStatement.setString(2, user.first_name);
            preparedStatement.setString(3, user.last_name);
            preparedStatement.setString(4, user.mobile_phone);
            preparedStatement.setString(5, user.home_town);
            preparedStatement.setString(6, user.photo_max_orig);
            preparedStatement.setInt(7, user.has_mobile);
            preparedStatement.setString(8, user.activities);
            preparedStatement.setInt(9, user.online);

            preparedStatement.executeUpdate();
        }
    }
}
