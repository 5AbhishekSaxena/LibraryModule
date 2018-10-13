package in.abhishek.LibraryModule.Activity;


import in.abhishek.LibraryModule.Data.Book;
import in.abhishek.LibraryModule.Data.Borrower;
import in.abhishek.LibraryModule.Data.SQLiteJDBCDriverConnection;

import static in.abhishek.LibraryModule.Utils.AppConstants.BOOK;
import static in.abhishek.LibraryModule.Utils.AppConstants.BORROWER;
import static in.abhishek.LibraryModule.Utils.AppConstants.ISSUE;
import static in.abhishek.LibraryModule.Utils.AppConstants.RETURN;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static in.abhishek.LibraryModule.Utils.UtilFunctions.println;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class MainScreen {

    private static String bookId;

    private static Borrower currentBorrower;
    private static Book currentBook;

    private static Scanner scanner;

    private MainScreen() {
        Borrower userDetails = LoginModule.getUserDetails();
        Book bookDetails = null;
        if (userDetails != null) {
            String name = userDetails.getName();
            String userId = userDetails.getId();
            String password = userDetails.getPassword();
            boolean hasIssued = userDetails.isHasIssued();
            String bookId = userDetails.getBookId();
            currentBorrower = new Borrower(name, userId, password, hasIssued, bookId);
            currentBorrower.setAdmin(new SQLiteJDBCDriverConnection().checkAdminStatus(userId));
            if (!(bookId == null)) {
                bookDetails = new SQLiteJDBCDriverConnection().getBookUsingUserId(userId);
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
        String invalidOptionSelected = "Invalid Option Selected.";
        String adminmenu = "7. Add Book (Admins Only)\n8. Delete Book (Admins Only)" +
                "\n9. Make Admin (Admins Only)\n10. Remove Admin (Admins Only)";
        println("Main Menu");
        println("1. Issue Book");
        println("2. Get details of a book.");
        println("3. View names of all books.");
        println("4. Get details of a borrower.");
        println("5. Return Book");
        println("6. Show issued Book (if any)");
        if (currentBorrower.isAdmin())
            println(adminmenu);
        println("99. Log out");
        println("Enter Choice: ");
        int x = scanner.nextInt();
        switch (x) {
            case 1:
                issueBook();
                break;
            case 2:
                println("Enter Book ID: ");
                bookId = scanner.next();
                currentBook = (Book) new SQLiteJDBCDriverConnection().getRecords(bookId, BOOK);
                if (currentBook != null) {
                    println("Book Found");
                    println(currentBook.details());
                } else
                    println("Book not found");
                resetBookRef();
                break;
            case 3:
                ArrayList<Book> books = new SQLiteJDBCDriverConnection().getAllBooks();
                if (books != null)
                    for (Book b : books)
                        println(b.toString());
                break;
            case 4:
                if (currentBorrower != null) {
                    println(currentBorrower.details());
                }
                break;
            case 5:
                if (currentBorrower.isHasIssued()) {
                    if (updateReturnIssueBookStatus(currentBorrower.getId(), currentBorrower.getBookId(), RETURN))
                        println("Book returned successfully.");
                    else
                        println("Book not returned.");
                } else
                    println("User haven't issued any book.");
                break;
            case 6:
                if (currentBorrower.getBookDetails() == null)
                    println("No book issued.");
                else
                    println(currentBorrower.getBookDetails().details());
                break;
            case 7:
                if (currentBorrower.isAdmin()) {
                    resetBookRef();
                    addBook();
                } else
                    println(invalidOptionSelected);
                break;
            case 8:
                if (currentBorrower.isAdmin()) {
                    deleteBook();
                } else
                    println(invalidOptionSelected);
                break;
            case 9:
                if (currentBorrower.isAdmin()) {
                    String adminId;
                    println("Enter user ID");
                    adminId = scanner.next().trim();
                    if (new SQLiteJDBCDriverConnection().makeAdmin(adminId))
                        println("Admin permissions granted");
                    else
                        println("Failed to give admin permissions");
                } else
                    println(invalidOptionSelected);
                break;
            case 10:
                if (currentBorrower.isAdmin()) {
                    String adminId;
                    println("Enter Admin ID");
                    adminId = scanner.next().trim();
                    if (new SQLiteJDBCDriverConnection().removeAdmin(adminId))
                        println("Admin permissions revoked");
                    else
                        println("Failed to revoke admin permissions");
                } else
                    println(invalidOptionSelected);
                break;
            case 99:
                logout();
                break;
            default:
                println(invalidOptionSelected);
                break;
        }

        setUpMainMenu();

    }

    private void issueBook() {
        if (currentBorrower != null) {
            if (!currentBorrower.isHasIssued()) {
                println("Please enter book ID:");
                bookId = scanner.next();
                currentBook = (Book) new SQLiteJDBCDriverConnection().getRecords(bookId, BOOK);
                if (currentBook == null)
                    println("Invalid book ID, book not found.");
                else {
                    if (currentBook.getIssuedBy() == null) {
                        if (updateReturnIssueBookStatus(currentBorrower.getId(), bookId, ISSUE))
                            println("Book Issued Successfully");
                    } else
                        println("Book is already issued by another user.");
                }
            } else
                println(String.format("User has already issued a book. \nBook Name: %s (%s)",
                        currentBorrower.getBookDetails().getName(), currentBorrower.getBookDetails().getId()));

        }
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

        int lastID = Integer.parseInt(new SQLiteJDBCDriverConnection().getLastID(BOOK));
        println("Assigned Book Id: " + ++lastID);
        id = String.valueOf(lastID);
        scanner.nextLine();
        println("Enter Book name: ");
        name = scanner.nextLine();
        println("Enter Book author: ");
        author = scanner.nextLine();
        println("Enter Book published by: ");
        publishedBy = scanner.nextLine();
        println("Enter Book published on: ");
        publishedOn = scanner.nextLine();
        println("Enter Book number of pages: ");
        numberOfpages = "";
        try {
            numberOfpages = String.valueOf(Integer.parseInt(scanner.nextLine()));
        } catch (InputMismatchException | NumberFormatException e) {
            println("Number of pages must be an integer.");
            println("book NOT added.");
            setUpMainMenu();
        }

        if (new SQLiteJDBCDriverConnection().insertRecord(BOOK, id, name, author, publishedBy, publishedOn, numberOfpages, null)) {
            println("Book Added successfully.");
        } else {
            println("Book NOT added.");
        }

    }

    private boolean updateReturnIssueBookStatus(String userId, String bookId, int type) {
        if (checkRecord(userId, BORROWER)) {
            if (checkRecord(bookId, BOOK)) {
                if (new SQLiteJDBCDriverConnection().updateReturnIssueStatusDB(currentBorrower.getId(), bookId, type)) {
                    currentBorrower.setBookId(bookId);
                    currentBorrower.updateHasIssued(type == ISSUE);
                    if (type == ISSUE)
                        currentBorrower.setBookDetails((Book) new SQLiteJDBCDriverConnection().getRecords(bookId, BOOK));
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
        if (type == BOOK) {
            Book book = (Book) new SQLiteJDBCDriverConnection().getRecords(id, type);
            return book != null;
        } else if (type == BORROWER) {
            Borrower borrower = (Borrower) new SQLiteJDBCDriverConnection().getRecords(id, type);
            return borrower != null;
        }
        return false;
    }

    private void logout() {
        println("Logging out....");
        resetRef();
        LoginModule.main(null);
    }

    private void deleteBook() {
        println("Delete Book...");
        println("Enter Book ID: (-1 to cancel operation)");
        String id = scanner.next();
        if (!(id == null)) {
            Book book = (Book) new SQLiteJDBCDriverConnection().getRecords(id, BOOK);
            if (book != null) {
                if (!(book.getIssuedBy() == null)) {
                    println(String.format("Book is issued by %s (User ID) and cannot be deleted.", book.getIssuedBy()));
                } else {
                    if (new SQLiteJDBCDriverConnection().deleteBookUsingID(id)) {
                        println("Book deleted successfully");
                        setUpMainMenu();
                    }
                }
            } else
                println("Invalid Book ID");
        } else
            println("Operation canceled");

        println("Book not deleted.");
    }
}