package in.abhishek.LibraryModule.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class SessionManager {

    //TODO: Fix SessionManager class

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "kdbc.mysql://localhost/Clients";

    private static final String USER = "username";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        Connection conn = null;
        Statement statement = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to the selected database....");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected database successfully.....");

            System.out.println("Creating table in given database....");
            statement = conn.createStatement();

            String sql_create = "CREATE TABLE " + SessionEntries.TABLE_NAME +
                    "(" + SessionEntries.ID_FIELD + "VARCHAR(2) NOT NULL, " +
                    SessionEntries.PASSWORD_FIELD + "VARCHAR(20) NOT NULL, " +
                    "PRIMARY KEY (" + SessionEntries.ID_FIELD + "))";

            statement.executeUpdate(sql_create);
            System.out.println("Created table in the given database......");
            //statement.executeUpdate("INSERT INTO Clients" + " VALUES ('1001', 'Simpson')")
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            if (statement != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
                    if (conn != null){
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
        System.out.println("GoodBye.....");
    }

    private static class SessionEntries{

        private static String TABLE_NAME = "Clients";

        private static String ID_FIELD = "_ID";
        private static String PASSWORD_FIELD = "password";
    }
}
