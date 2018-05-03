
import java.sql.*;

public class Connector {

    private final String urlOfDatabase = "jdbc:mysql://127.0.0.1:3306/summery_db?user=root&password=root&useSSL=false";
    private Connection connection;
    private Statement statement;

    public Connector() {
        try {
            connection = DriverManager.getConnection(urlOfDatabase);
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


    public void addCookie(String userID, String cookie){

        System.out.println("User: " + userID + " Cookie: "+cookie);
        try {
            statement.executeUpdate("UPDATE user SET cookies = '" + cookie + "' WHERE iduser = " + userID);

        } catch (SQLException e) {
           e.printStackTrace();
           System.out.println("connection failed");
        }
    }
}
