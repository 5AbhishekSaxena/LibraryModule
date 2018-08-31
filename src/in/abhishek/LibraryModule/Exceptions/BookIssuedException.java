package in.abhishek.LibraryModule.Exceptions;

public class BookIssuedException extends Exception{
    public BookIssuedException() {
        super("Book is already Issued");
    }
}
