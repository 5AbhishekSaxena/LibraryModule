package in.abhishek.LibraryModule;

import in.abhishek.LibraryModule.Data.Book;
import in.abhishek.LibraryModule.Data.Borrower;
import in.abhishek.LibraryModule.Exceptions.LibraryException;

import java.util.ArrayList;
import java.util.Scanner;

public class MainScreen {
    private static final int BOOKS = 1;
    private static final int BORROWER = 2;
    private static String name;
    private static String password;
    private static String _ID;
    private static String bookId;

    private static Borrower borrower;

    private static ArrayList<Book> books;
    private static ArrayList<Borrower> borrowers;
    private static Scanner scanner;
    private static Book bookDetails;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        addBooks();
        addUser();
        //borrower.issueBook("103");
        //System.out.println(borrower.toString());
        setUpMainMenu();
    }

    private static void setUpMainMenu() {
        System.out.println("Main Menu");
        System.out.println("1. Issue Book");
        System.out.println("2. Get details of a book");
        System.out.println("3. Close Main Menu");
        System.out.println("Enter Choice: ");
        int x = scanner.nextInt();
        switch (x) {
            case 1:
                try {
                    String borrowId = "1";
                    System.out.println("Please enter borrow's ID: " + borrowId);
                    System.out.println("Please enter book ID:");
                    bookId = scanner.next();
                    Borrower borrower1 = null;
                    for (Borrower b : borrowers) {
                        if (b.getId().equals(borrowId)) {
                            borrower1 = b;
                        }
                    }
                    //borrower1 = borrowers.get(0);
                    //System.out.println("Borrow's details \n" + borrower1.toString());

                    if (borrower1 == null)
                        throw new LibraryException("User not Found, Invalid credential");
                    if (borrower1.issueBook(bookId))
                        System.out.println("Book Issued Successfully");
                    else
                        System.out.println("Book not Issued");
                } catch (LibraryException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case 2:
                System.out.println("Enter Book ID: ");
                bookId = scanner.next();
                for (Book book : books) {
                    if (book.getId().equals(bookId)) {
                        System.out.println(book.details());
                    }
                }
                break;
        }
        setUpMainMenu();
    }

    private static void addUser() {
        /*scanner = new Scanner(System.in);
        System.out.print("Enter Name: ");
        name = scanner.nextLine();
        System.out.print("\nEnter Password: ");
        password = scanner.nextLine();
        System.out.print("\nEnter ID: ");
        _ID = scanner.nextLine();

        borrower = new Borrower(name, _ID, password, false);*/
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

    public static ArrayList<Book> getBooks() {
        return books;
    }

    private void getDetails(String id, int type) {
        if (type == BOOKS) {
            for (Book b : books) {
                if (b.getId().equals(id)) {
                    System.out.println(b.details());
                }
            }
        } else if (type == BORROWER) {
            for (Borrower b : borrowers) {
                if (b.getId().equals(id)) {
                    System.out.println(b.details());
                }

            }
        }

    }
}
