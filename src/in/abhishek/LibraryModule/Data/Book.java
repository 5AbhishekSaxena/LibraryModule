package in.abhishek.LibraryModule.Data;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class Book extends Information {

    //private static String issuedBy;
    private String author;
    private String publishedBy;
    private String publishedOn;
    private int numberOfPages;
    private String issuedBy;

    Book(String name, String id, String author, String publishedBy, String publishedOn, int numberOfPages, String issuedBy) {
        super(name, id);
        this.author = author;
        this.publishedBy = publishedBy;
        this.publishedOn = publishedOn;
        this.numberOfPages = numberOfPages;
        if (issuedBy != null)
            this.issuedBy = issuedBy;
        else
            this.issuedBy = null;
    }

    @Override
    public String details() {
        return "Book Details\nBook Name: " + name + "\nBook ID: " + _ID + "\nAuthor: " + author +
                "\nPublished By: " + publishedBy + "\nPublushed On: " + publishedOn + "\nNumber of Pages: " + numberOfPages +
                "\nIssued By: " + (issuedBy.equals("-1") ? "None" : issuedBy + "(User ID)");
    }

    @Override
    public String toString() {
        return "Book Name: " + name;
    }
}
