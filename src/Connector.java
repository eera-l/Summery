
import java.sql.*;

public class Connector {

    private Connection connection;
    private Statement statement;

    public Connector() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/summery_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "root");
            statement = connection.createStatement();
        } catch (SQLException sqlEx) {

            sqlEx.printStackTrace();

        }
    }

    public String searchByID(String parameterToSearch, String id) {

        String sqlString = "";
        try {
            ResultSet rs = statement
                    .executeQuery("SELECT " + parameterToSearch + " FROM user WHERE iduser = " + id);
            while (rs.next()) {
                sqlString = rs.getString(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return sqlString;
    }

    public String searchByIPAddress(String parameterToSearch, String ipAddress) {
        String sqlString = "";
        try {
            ResultSet rs = statement
                    .executeQuery("SELECT " + parameterToSearch + " FROM user WHERE ip_address = '" + ipAddress + "'");
            while (rs.next()) {
                sqlString = rs.getString(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return sqlString;
    }


    public void updateById(String parameterToUpdate, String newParameter, String id) {

        try {
            int rows = statement.executeUpdate("UPDATE user SET " + parameterToUpdate + " = "
                    + newParameter + " WHERE iduser = " + id);
            System.out.println("Updated rows: " + rows);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public void updateByIpAddress(String parameterToUpdate, String newParameter, String ipAddress) {

        try {
            int rows = statement.executeUpdate("UPDATE user SET " + parameterToUpdate + " = "
                    + newParameter + " WHERE ip_address = '" + ipAddress + "'");
            System.out.println("Updated rows: " + rows);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public void insert(String values) {

        try {
            statement.executeUpdate("INSERT INTO user (iduser, ip_address, cookies) VALUES (" + values + ")");

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public void delete(String whereStatement) {

        try {
            statement.executeUpdate("DELETE FROM user WHERE " + whereStatement);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }


    public void addCookie(String userID, String ip, String cookie){
        System.out.println("User: " + userID + " Cookie: "+cookie);
        try{
            statement = connection.createStatement();
            statement.executeUpdate("insert user set iduser = '" + userID + "', ip_address  = '" + ip + "', cookies ='"+cookie+"';");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connection failed");
        }
    }
}