
import java.sql.*;

public class Connector {



    public static void addCookie(int userID, String ip, String cookie){
        Connection dbconnect;
        Statement statement;

        System.out.println("User: " + userID + " Cookie: "+cookie);
        try{
            dbconnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/summery_db_", "root", "root");
            statement = dbconnect.createStatement();
            statement.executeUpdate("insert user set iduser = " + userID + ", ip_address  = '" + ip + "', cookies ='"+cookie+"';");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connection failed");
        }
    }
}
