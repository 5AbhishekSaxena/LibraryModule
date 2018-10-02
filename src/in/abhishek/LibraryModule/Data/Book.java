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

    public Book(String name, String id, String author, String publishedBy, String publishedOn, int numberOfPages, String issuedBy) {
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

   public Book getBookDetails(String id){
        return new Book("book_name", id, "book_author",
                "book_publishedby", "book_publishedby", 101, issuedBy);
    }
/*
    public Book getBookDetails(String id) {
        return new Book(name, id, author,
                publishedOn, publishedBy, numberOfPages, issuedBy);
    }*/

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {

        this.numberOfPages = numberOfPages;
    }


    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
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
