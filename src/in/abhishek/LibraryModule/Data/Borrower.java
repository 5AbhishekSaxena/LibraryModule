package in.abhishek.LibraryModule.Data;

import in.abhishek.LibraryModule.Utils.AppConstants;
import in.abhishek.LibraryModule.Exceptions.LibraryException;
import in.abhishek.LibraryModule.Activity.MainScreen;

import static in.abhishek.LibraryModule.Utils.UtilFunctions.encrypt;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class Borrower extends Information {

    private String password;
    private boolean hasIssued;
    private String bookId;
    private Book bookDetails;

    public Borrower(String name, String id, String password, boolean hasIssued, String bookId) {
        super(name, id);
        this.password = password;
        this.hasIssued = hasIssued;
        this.bookId = bookId;
    }

    /*public Borrower(String name, String id, String password, boolean hasIssued, String bookId) {
        this(name, id, password, hasIssued);
        if (hasIssued) {
            this.bookId = bookId;
            for (Book b : MainScreen.getBook()) {
                if (b.getId().equals(bookId)) {
                    bookDetails = b;
                    System.out.println(bookDetails.details());
                }
            }
        }
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasIssued() {
        return hasIssued;
    }

    public void updateHasIssued(boolean hasIssued) {
        this.hasIssued = hasIssued;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Book getBookDetails() {
        return bookDetails;
    }

    public void setBookDetails(Book bookDetails) {
        this.bookDetails = bookDetails;
    }

    @Override
    public String details() {
        StringBuilder stringBuilder = new StringBuilder("Name: " + name + "\nID: " + _ID + "\nPassword: " + password
                + "\nhasIssued: " + (hasIssued ? "" : "No book issued."));
        if (hasIssued) {
            stringBuilder.append("\n");
            stringBuilder.append(bookDetails.details());
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Borrower{" +
                "password='" + password + '\'' +
                ", hasIssued=" + hasIssued +
                ", bookId='" + bookId + '\'' +
                ", _ID='" + _ID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
