
import java.sql.*;

public class Connector {



    public static void addCookie(String userID, String cookie){
        ResultSet resultSet;
        Connection dbconnect;
        Statement statement;

        System.out.println("User: " + userID + " Cookie: "+cookie);
//        try{
//            dbconnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "root");
//            statement = dbconnect.createStatement();
//            statement.executeUpdate("insert user set userID = '" + userID + "' where userName = '" + cookie + "';");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("connection failed");
//        }
    }
}
