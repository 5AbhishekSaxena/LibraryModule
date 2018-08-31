package in.abhishek.LibraryModule.Data;

import in.abhishek.LibraryModule.Utils.AppConstants;
import in.abhishek.LibraryModule.Exceptions.LibraryException;
import in.abhishek.LibraryModule.Activity.MainScreen;
import in.abhishek.LibraryModule.Utils.UtilFunctions;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class Borrower extends Information {

    private String password;
    private boolean hasIssued;
    private String bookId;
    private Book bookDetails;

    public Borrower(String name, String id, String password, boolean hasIssued) {
        super(name, id);
        this.password = UtilFunctions.encrypt(password);
        this.hasIssued = hasIssued;
        bookId = null;
    }

    public Borrower(String name, String id, String password, boolean hasIssued, String bookId) {
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
    }

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

    public boolean issueBook(String bookId) throws LibraryException {
        bookDetails = (Book) MainScreen.find(bookId, AppConstants.BOOK);
        if (bookDetails == null)
            throw new LibraryException("Book Not Found");
        if (bookDetails.getIssuedBy() != null)
            throw new LibraryException("Book is already Issued.");

        if (hasIssued)
            throw new LibraryException("User has already issued a book from the Library");
        else {
            this.bookId = bookId;
            bookDetails.setIssuedBy(_ID);
            hasIssued = true;
            return true;
        }
    }

    @Override
    public String details() {
        StringBuilder stringBuilder = new StringBuilder("Name: " + name + "\nID: " + _ID + "\nPassword: " + password + "\nhasIssued: " + hasIssued);
        if (hasIssued) {
            stringBuilder.append("\n");
            stringBuilder.append(bookDetails.details());
        }
        return stringBuilder.toString();
    }
}
