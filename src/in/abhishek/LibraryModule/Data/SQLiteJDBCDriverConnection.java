package in.abhishek.LibraryModule.Data;

import in.abhishek.LibraryModule.Utils.AppConstants;
import in.abhishek.LibraryModule.Utils.UtilFunctions;
import static in.abhishek.LibraryModule.Utils.UtilFunctions.println;

import java.sql.*;

/**
 * Created by Abhishek Saxena on 01-10-2018.
 */

public class SQLiteJDBCDriverConnection {
    private Connection conn;

    /**
     * Connect to a sample database
     */
    private Connection connect() {
        conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:D:\\IdeaProjects\\LibraryModule\\library.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            println(e.getMessage());
        }
        return conn;
    }

    public boolean insertRecord(int type, String... args) {
        StringBuilder param = new StringBuilder();
        for (int i = 0; i < args.length; i++){
            param.append("?");
            if(i < args.length - 1)
                param.append(", ");
        }
        String sqlInsertBook = String.format(("INSERT INTO %s VALUES (" + param.toString() + ")"), (type == AppConstants.BORROWER ? "users" : "books"));
        conn = connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertBook);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setString(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            println(e.getMessage());
        } finally {
            closeDBConnection(conn);
        }
        return false;
    }

    public Object getRecords(String _id, int type) {
        conn = this.connect();
        String sqlGet = String.format("select * from %s where _id = ?", (type == AppConstants.BORROWER ? "users" : "books"));
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlGet);
            preparedStatement.setString(1, _id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return (type == AppConstants.BORROWER ? getUserFromQuery(resultSet) : getBookFromQuery(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeDBConnection(conn);
        }
        return null;
    }

    public boolean updateReturnIssueStatusDB(String userId, String bookId, int type) {
        conn = this.connect();
        try {
            String sqlUpdateHasIssued = "update users set hasIssued = ? where _id= ?";
            PreparedStatement psUpdateHasIssued = conn.prepareStatement(sqlUpdateHasIssued);
            psUpdateHasIssued.setString(1, (type == AppConstants.ISSUE ? "true" : "false"));
            psUpdateHasIssued.setString(2, userId);

            String sqlUpdateBookId = "update users set bookid = ? where _id= ?";
            PreparedStatement psUpdateBookId = conn.prepareStatement(sqlUpdateBookId);
            psUpdateBookId.setString(1, (type == AppConstants.ISSUE ? bookId : "-1"));
            psUpdateBookId.setString(2, userId);

            String sqlUpdateUserId = "update books set issuedby = ? where _id= ?";
            PreparedStatement psUpdateUserId = conn.prepareStatement(sqlUpdateUserId);
            psUpdateUserId.setString(1, (type == AppConstants.ISSUE ? userId : "-1"));
            psUpdateUserId.setString(2, bookId);

            if (psUpdateHasIssued.executeUpdate() == 1) {
                if (psUpdateBookId.executeUpdate() == 1) {
                    if (psUpdateUserId.executeUpdate() == 1)
                        return true;
                    else
                        println("User Id not updated in books database.");
                } else
                    println("Book Id in users database not updated");
            } else
                println("Issued Status not updated in User database.");
            return false;
        } catch (SQLException e) {
            println(e.getMessage());
            e.printStackTrace();
        } finally {
            closeDBConnection(conn);
        }
        return false;
    }

    public Book getBookUsingUserId(String userId) {
        conn = this.connect();
        String sqlGetBook = "SELECT * FROM books where issuedby = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlGetBook);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getBookFromQuery(resultSet);
        } catch (SQLException e) {
            println(e.getMessage());
        } finally {
            closeDBConnection(conn);
        }
        return null;
    }

    private Book getBookFromQuery(ResultSet resultSet) {
        try {
            if (resultSet.isClosed()) {
                return null;
            }

            String id = resultSet.getString("_id");
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            String publishedBy = resultSet.getString("publishedBy");
            String publishedOn = resultSet.getString("publishedOn");
            //int numberOfPages = Integer.parseInt(resultSet.getString("numberOfPages"));
            String numberOfPages = resultSet.getString("numberOfPages");
            String issuedBy = resultSet.getString("issuedby");
            return new Book(name, id, author, publishedBy, publishedOn, 123, issuedBy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Borrower getUserFromQuery(ResultSet resultSet) {
        try {
            if (resultSet.isClosed()) {
                return null;
            }
            String id = resultSet.getString("_id");
            String name = resultSet.getString("name");
            String pass = resultSet.getString("password");
            boolean hasIssued = Boolean.parseBoolean(resultSet.getString("hasIssued"));
            String bookId = resultSet.getString("bookid");
            return new Borrower(name, id, pass, hasIssued, bookId);
        } catch (SQLException e) {
            println(e.getMessage());
        }
        return null;
    }

    private void closeDBConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            println(e.getMessage());
        }
    }
}