package in.abhishek.LibraryModule.Activity;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

import in.abhishek.LibraryModule.Data.Book;
import in.abhishek.LibraryModule.Data.Borrower;
import in.abhishek.LibraryModule.Data.SQLiteJDBCDriverConnection;
import in.abhishek.LibraryModule.Utils.AppConstants;

import java.util.Scanner;

import static in.abhishek.LibraryModule.Utils.UtilFunctions.println;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class MainScreen {

    /**
     * TODO: Make Staff architecture - Regular Staff and Admin.
     */

    private static String bookId;
    private static String borrowId;

    private static Borrower currentBorrower;
    private static Book currentBook;

    private static Scanner scanner;

    private MainScreen() {
        Borrower userDetails = LoginModule.getUserDetails();
        Book bookDetails = LoginModule.getBookDetails();
        if (userDetails != null) {
            String name = userDetails.getName();
            String id = userDetails.getId();
            String password = userDetails.getPassword();
            boolean hasIssued = userDetails.isHasIssued();
            String bookId = userDetails.getBookId();
            currentBorrower = new Borrower(name, id, password, hasIssued, bookId);
            if (!bookId.equals("-1")) {
                if (bookDetails != null) {
                    currentBook = bookDetails;
                    currentBorrower.setBookDetails(currentBook);
                }
            }
            if (bookDetails != null)
                currentBook = bookDetails;
        } else {
            println("Invalid User login.");
            logout();
        }
    }

    public static void main(String[] args) {
        MainScreen mainScreen = new MainScreen();
        scanner = new Scanner(System.in);
        mainScreen.setUpMainMenu();
    }

    private void setUpMainMenu() {
        println("Main Menu");
        println("1. Issue Book");
        println("2. Get details of a book.");
        println("3. Get details of a borrower.");
        println("4. Add Book");
        println("5. Return Book");
        println("99. Log out");
        println("Enter Choice: ");
        int x = scanner.nextInt();
        switch (x) {
            case 1:
                println("Please enter book ID:");
                bookId = scanner.next();

                if (currentBorrower != null) {
                    if (updateReturnIssueBookStatus(currentBorrower.getId(), bookId, AppConstants.ISSUE))
                        println("Book Issued Successfully");
                }
                break;
            case 2:
                println("Enter Book ID: ");
                bookId = scanner.next();
                currentBook = (Book) new SQLiteJDBCDriverConnection().getRecords(bookId, AppConstants.BOOK);
                if (currentBook != null) {
                    println("Book Found");
                    println(currentBook.details());
                } else
                    println("Book not found");
                resetBookRef();
                break;
            case 3:
                if (currentBorrower != null) {
                    println(currentBorrower.details());
                }
                break;
            case 4:
                resetBookRef();
                addBook();
                break;
            case 5:
                if (currentBorrower.isHasIssued()) {
                    if (updateReturnIssueBookStatus(currentBorrower.getId(), currentBorrower.getBookId(), AppConstants.RETURN))
                        println("Book returned successfully.");
                    else
                        println("Book not returned.");
                } else
                    println("User haven't issued any book.");
                break;
            case 10:
                println(currentBorrower.getBookDetails().details());
                break;
            case 99:
                logout();
            default:
                println("Invalid Option Selected.");
                break;
        }
        setUpMainMenu();
    }

    private void resetBookRef() {
        currentBook = null;
    }

    private void resetBorrowerRef() {
        currentBorrower = null;
    }
    private void resetRef() {
        resetBookRef();
        resetBorrowerRef();
    }

    private void addBook() {
        String id, name, author, publishedBy, publishedOn, numberOfpages;

        println("Enter Book Id: ");
        id = scanner.next();
        println("Enter Book name: ");
        name = scanner.next();
        println("Enter Book author: ");
        author = scanner.next();
        println("Enter Book published by: ");
        publishedBy = scanner.next();
        println("Enter Book published on: ");
        publishedOn = scanner.next();
        println("Enter Book number of pages: ");
        numberOfpages = scanner.next();

        /*if (new SQLiteJDBCDriverConnection().insertBook(id, name, author, publishedBy, publishedOn, numberOfpages, "-1")) {
            println("Book Added successfully.");
        } else {
            println("Book NOT added.");
        }*/
        if (new SQLiteJDBCDriverConnection().insertRecord(AppConstants.BOOK, id, name, author, publishedBy, publishedOn, numberOfpages, "-1")) {
            println("Book Added successfully.");
        } else {
            println("Book NOT added.");
        }

    }

    private boolean updateReturnIssueBookStatus(String userId, String bookId, int type) {
        if (checkRecord(userId, AppConstants.BORROWER)) {
            if (checkRecord(bookId, AppConstants.BOOK)) {
                if (new SQLiteJDBCDriverConnection().updateReturnIssueStatusDB(currentBorrower.getId(), bookId, type)) {
                    currentBorrower.setBookId(bookId);
                    currentBorrower.updateHasIssued(type == AppConstants.ISSUE);
                    if (type == AppConstants.ISSUE)
                        currentBorrower.setBookDetails((Book) new SQLiteJDBCDriverConnection().getRecords(bookId, AppConstants.BOOK));
                    else
                        currentBorrower.setBookDetails(null);
                    //UtilFunctions.updateUser(currentBorrower);
                    println("User Details updated.");
                    return true;
                }
            } else
                println("Invalid Book Id.");
        } else
            println("Invalid User Id.");
        return false;
    }

    private boolean checkRecord(String id, int type) {
        if (type == AppConstants.BOOK) {
            Book book = (Book) new SQLiteJDBCDriverConnection().getRecords(id, type);
            return book != null;
        } else if (type == AppConstants.BORROWER) {
            Borrower borrower = (Borrower) new SQLiteJDBCDriverConnection().getRecords(id, type);
            return borrower != null;
        }
        return false;
    }

    private void logout(){
        println("Logging out....");
        resetRef();
        LoginModule.main(null);
    }
}