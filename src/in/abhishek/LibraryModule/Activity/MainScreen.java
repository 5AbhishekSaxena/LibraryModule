package in.abhishek.LibraryModule.Activity;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

import in.abhishek.LibraryModule.Data.Book;
import in.abhishek.LibraryModule.Data.Borrower;
import in.abhishek.LibraryModule.Exceptions.LibraryException;

import java.util.ArrayList;
import java.util.Scanner;

import static in.abhishek.LibraryModule.Utils.AppConstants.BORROWER;
import static in.abhishek.LibraryModule.Utils.AppConstants.BOOK;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class MainScreen {

    /**
     *  TODO: Make Login Module.
     *  TODO: Make Sign up Module.
     *  TODO: Make Staff architecture - Regular Staff and Admin.
     */

    /*private static String name;
    private static String password;
    private static String _ID;*/
    private static String bookId;
    private static String borrowId;

    private static Borrower currentBorrower;

    private static ArrayList<Book> books;
    private static ArrayList<Borrower> borrowers;
    private static Scanner scanner;
    private static Book currentBook;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        addBooks();
        addUser();
        setUpMainMenu();
    }

    private static void setUpMainMenu() {
        try {
            System.out.println("Main Menu");
            System.out.println("1. Issue Book");
            System.out.println("2. Get details of a book.");
            System.out.println("3. Get details of a borrower.");
            System.out.println("4. Return Book");
            System.out.println("5. Close Main Menu");
            System.out.println("Enter Choice: ");
            int x = scanner.nextInt();
            switch (x) {
                case 1:
                    resetRef();
                    System.out.println("Please enter borrow's ID: ");
                    borrowId = scanner.next();
                    currentBorrower = (Borrower) find(borrowId, BORROWER);
                    System.out.println("Please enter book ID:");
                    bookId = scanner.next();
                    currentBook = (Book) find(bookId, BOOK);

                    if (currentBorrower != null) {
                        if (currentBook != null) {
                            if (currentBorrower.issueBook(bookId))
                                System.out.println("Book Issued Successfully");
                        }
                    }
                    break;
                case 2:
                    resetRef();
                    System.out.println("Enter Book ID: ");
                    bookId = scanner.next();
                    if (!getDetails(bookId, BOOK))
                        System.out.println("Book not found");
                    break;
                case 3:
                    resetRef();
                    System.out.println("Enter Borrower ID: ");
                    borrowId = scanner.next();
                    if (!getDetails(borrowId, BORROWER))
                        System.out.println("Borrow not found");
                    break;
                case 4:
                    resetRef();
                    System.out.println("Enter Book ID: ");
                    bookId = scanner.next();
                    currentBook = (Book) find(bookId, BOOK);
                    System.out.println("Enter Borrow ID: ");
                    borrowId = scanner.next();
                    currentBorrower = (Borrower) find(borrowId, BORROWER);
                    if (!returnBook(currentBorrower, currentBook)) {
                        throw new LibraryException("Book not returned");
                    } else {
                        System.out.println("Book returned successfully.");
                    }
                    break;
                default:
                    System.out.println("Invalid Option Selected.");
                    break;
            }
        } catch (LibraryException e) {
            System.out.println(e.getMessage());
        }
        setUpMainMenu();
    }

    private static void resetRef() {
        currentBorrower = null;
        currentBook = null;
    }

    private static void addUser() {
        /*scanner = new Scanner(System.in);
        System.out.print("Enter Name: ");
        name = scanner.nextLine();
        System.out.print("\nEnter Password: ");
        password = scanner.nextLine();
        System.out.print("\nEnter ID: ");
        _ID = scanner.nextLine();

        currentBorrower = new Borrower(name, _ID, password, false);*/
        if (borrowers == null)
            borrowers = new ArrayList<>();

        borrowers.add(new Borrower("b1", "1", "123", false));
        borrowers.add(new Borrower("b2", "2", "123", false));
        borrowers.add(new Borrower("b3", "3", "123", false));
        borrowers.add(new Borrower("b4", "4", "123", false));
        borrowers.add(new Borrower("b5", "5", "123", false));

    }

    private static void addBooks() {
        if (books == null)
            books = new ArrayList<>();

        books.add(new Book("Book1", "101", "Author1", "Publisher1",
                "123", 101, null));
        books.add(new Book("Book2", "102", "Author2", "Publisher2",
                "123", 101, null));
        books.add(new Book("Book3", "103", "Author3", "Publisher3",
                "123", 101, null));
        books.add(new Book("Book4", "104", "Author4", "Publisher4",
                "123", 101, null));
    }

    public static ArrayList<Book> getBook() {
        return books;
    }

    private static boolean getDetails(String id, int type) throws LibraryException {
        if (type == BOOK) {
            currentBook = (Book) find(id, type);
            if (currentBook != null) {
                System.out.println(currentBook.details());
                return true;
            }
        } else if (type == BORROWER) {
            currentBorrower = (Borrower) find(id, type);
            if (currentBorrower != null) {
                System.out.println(currentBorrower.details());
                return true;
            }
        }
        return false;
    }

    private static boolean returnBook(Borrower borrow, Book book) {
        if (book.getIssuedBy() != null) {
            if (borrow.isHasIssued()) {
                borrow.updateHasIssued(false);
                borrow.setBookId(null);
                book.setIssuedBy(null);
                return true;
            }
        }
        return false;
    }

    public static Object find(String id, int type) throws LibraryException {
        if (type == BOOK) {
            for (Book b : books) {
                if (b.getId().equals(id)) {
                    return b;
                }
            }
            throw new LibraryException("Invalid book ID.");

        } else if (type == BORROWER) {
            for (Borrower b : borrowers) {
                if (b.getId().equals(id)) {
                    return b;
                }
            }
            throw new LibraryException("Invalid borrower ID.");
        }
        return null;
    }
}
