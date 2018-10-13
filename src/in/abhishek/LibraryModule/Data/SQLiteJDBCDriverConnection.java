package in.abhishek.LibraryModule.Data;

import in.abhishek.LibraryModule.Utils.AppConstants;
import org.sqlite.SQLiteConfig;

import static in.abhishek.LibraryModule.Utils.AppConstants.ISSUE;
import static in.abhishek.LibraryModule.Utils.AppConstants.BOOK;
import static in.abhishek.LibraryModule.Utils.AppConstants.BORROWER;
import static in.abhishek.LibraryModule.Utils.UtilFunctions.println;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Abhishek Saxena on 01-10-2018.
 */

public class SQLiteJDBCDriverConnection {
    private Connection conn;

    private final String invalidID = "Invalid Id entered";

    /**
     * Connect to a sample database
     */
    private Connection connect() {
        conn = null;
        try {
            // db parameters
            String url = String.format("jdbc:sqlite:%s\\src\\in\\abhishek\\LibraryModule\\library.db",
                    AppConstants.PROJECT_LOCATION);
            SQLiteConfig sqLiteConfig = new SQLiteConfig();
            sqLiteConfig.enforceForeignKeys(true);
            // create a connection to the database
            conn = DriverManager.getConnection(url, sqLiteConfig.toProperties());

            println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            println("trouble connecting database. Contact Admin.");
        }
        return conn;
    }

    public boolean insertRecord(int type, String... args) {
        StringBuilder param = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            param.append("?");
            if (i < args.length - 1)
                param.append(", ");
        }
        String sqlInsertBook = String.format(("INSERT INTO %s VALUES (%s)"), (type == BORROWER ? "users" : "books"),
                param.toString());
        conn = connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertBook);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setString(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();
            if (type == BORROWER) {
                String userId = args[0];
                if (userId.equals("100")) {
                    makeAdmin(userId);
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeDBConnection(conn);
        }
    }

    public Object getRecords(String _id, int type) {
        conn = this.connect();
        String sqlGet = String.format("select * from %s where _id = ?", (type == BORROWER ? "users" : "books"));
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlGet);
            preparedStatement.setString(1, _id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return (type == BORROWER ? getUserFromQuery(resultSet) : getBookFromQuery(resultSet));
        } catch (SQLException e) {
            println("Failed to get the record.");
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
            psUpdateHasIssued.setString(1, (type == ISSUE ? "true" : "false"));
            psUpdateHasIssued.setString(2, userId);

            String sqlUpdateBookId = "update users set bookid = ? where _id= ?";
            PreparedStatement psUpdateBookId = conn.prepareStatement(sqlUpdateBookId);
            psUpdateBookId.setString(1, (type == ISSUE ? bookId : null));
            psUpdateBookId.setString(2, userId);

            String sqlUpdateUserId = "update books set issuedby = ? where _id= ?";
            PreparedStatement psUpdateUserId = conn.prepareStatement(sqlUpdateUserId);
            psUpdateUserId.setString(1, (type == ISSUE ? userId : null));
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
            println(String.format("Failed to %s book.", (type == ISSUE ? "issue" : "return")));
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
            println("Failed to get the book details.");
        } finally {
            closeDBConnection(conn);
        }
        return null;
    }

    private Book getBookFromQuery(ResultSet resultSet) throws SQLException {
        if (resultSet.isClosed()) {
            return null;
        }

        String id = resultSet.getString("_id");
        String name = resultSet.getString("name");
        String author = resultSet.getString("author");
        String publishedBy = resultSet.getString("publishedBy");
        String publishedOn = resultSet.getString("publishedOn");
        String issuedBy = resultSet.getString("issuedby");
        String numberOfPages = resultSet.getString("numberOfPages");
        return new Book(name, id, author, publishedBy, publishedOn, numberOfPages, issuedBy);
    }

    private Borrower getUserFromQuery(ResultSet resultSet) throws SQLException {
        if (resultSet.isClosed()) {
            return null;
        }
        String id = resultSet.getString("_id");
        String name = resultSet.getString("name");
        String pass = resultSet.getString("password");
        boolean hasIssued = Boolean.parseBoolean(resultSet.getString("hasIssued"));
        String bookId = resultSet.getString("bookid");
        return new Borrower(name, id, pass, hasIssued, bookId);
    }

    private void closeDBConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            println("Failed to close connection");
        }
    }

    public boolean deleteBookUsingID(String id) {
        conn = this.connect();
        String sqlDeleteBook = "delete from books where _id = ?";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlDeleteBook);
            preparedStatement.setString(1, id);
            if (preparedStatement.executeUpdate() == 1)
                return true;
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public ArrayList<Book> getAllBooks() {
        conn = this.connect();
        String sqlGetAllBooks = "select * from books";

        ArrayList<Book> books = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlGetAllBooks);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                println("No Books found.");
                return null;
            } else {
                do {
                    String id = resultSet.getString("_id");
                    String name = resultSet.getString("name");
                    String author = resultSet.getString("author");
                    String publishedBy = resultSet.getString("publishedBy");
                    String publishedOn = resultSet.getString("publishedOn");
                    String issuedBy = resultSet.getString("issuedby");
                    String numberOfPages = resultSet.getString("numberOfPages");
                    books.add(new Book(name, id, author, publishedBy, publishedOn, numberOfPages, issuedBy));
                } while (resultSet.next());

                return books;
            }
        } catch (SQLException e) {
            return null;
        } finally {
            closeDBConnection(conn);
        }
    }

    public String getLastID(int type) {
        conn = this.connect();

        String lastId;
        String sqlLastID = String.format("select * from %s", (type == BOOK ? "books" : "users"));
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlLastID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                if (type == BOOK)
                    lastId = "0";
                else if (type == BORROWER)
                    lastId = "99";
                else
                    lastId = null;
            } else {
                do {
                    lastId = resultSet.getString("_id");
                } while (resultSet.next());
            }

            return lastId;
        } catch (SQLException e) {
            println("Failed to get the last user ID");
            println(invalidID);
        } finally {
            closeDBConnection(conn);
        }
        return null;
    }

    public boolean makeAdmin(String id) {
        conn = this.connect();
        String sqlMakeAdmin = "insert into admins values(?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlMakeAdmin);
            preparedStatement.setString(1, id);

            if (preparedStatement.executeUpdate() == 1)
                return true;

        } catch (SQLException e) {
            println(invalidID);
        } finally {
            closeDBConnection(conn);
        }
        return false;
    }

    public boolean removeAdmin(String adminId) {
        if (!adminId.equals("100")) {
            conn = this.connect();
            String sqlRemoveAdmin = "delete from admins where admin_id = ?";
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(sqlRemoveAdmin);
                preparedStatement.setString(1, adminId);

                if (preparedStatement.executeUpdate() == 1)
                    return true;
                else
                    println(invalidID);

            } catch (SQLException e) {
                return false;
            } finally {
                closeDBConnection(conn);
            }
        } else
            println("Insufficient permissions.");

        return false;
    }

    public boolean checkAdminStatus(String userId) {
        conn = this.connect();
        String sqlCheckAdmin = "select * from admins where admin_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlCheckAdmin);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getString("admin_id").equals(userId))
                return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeDBConnection(conn);
        }
        return false;
    }
}