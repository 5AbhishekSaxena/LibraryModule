package in.abhishek.LibraryModule.Data;

import in.abhishek.LibraryModule.Exceptions.LibraryException;
import in.abhishek.LibraryModule.MainScreen;
import in.abhishek.LibraryModule.UtilFunctions;

import java.util.ArrayList;


public class Borrower extends Information {

    private String password;
    private boolean hasIssued;
    private String bookId;
    private Book bookDetails;

    public Borrower(String name, String id, String password, boolean hasIssued) {
        this.name = name;
        this._ID = id;
        this.password = UtilFunctions.encrypt(password);
        this.hasIssued = hasIssued;
        bookId = null;
    }

    public Borrower(String name, String id, String password, boolean hasIssued, String bookId) {
        this(name, id, password, hasIssued);
        if (hasIssued) {
            this.bookId = bookId;
            for (Book b : MainScreen.getBooks()) {
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

    public void setHasIssued(boolean hasIssued) {
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

    public boolean issueBook(String bookId) {
        ArrayList<Book> books = MainScreen.getBooks();
        //details = new Book();
        try {
            if (!hasIssued) {
                this.bookId = bookId;
                bookDetails = null;
                for (Book book : books) {
                    if (bookId.equals(book.getId())) {
                        bookDetails = book;
                        book.setIssuedBy(_ID);
                        hasIssued = true;
                        break;
                    }
                }
                if (bookDetails == null)
                    throw new LibraryException("Book Not Found");
                return true;
            }else
                throw new LibraryException("User has already issued a book from the Library");
        } catch (LibraryException e) {
            System.out.println(e.getMessage());
        }
        return false;
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
