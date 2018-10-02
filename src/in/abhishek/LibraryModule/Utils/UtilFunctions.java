package in.abhishek.LibraryModule.Utils;

import in.abhishek.LibraryModule.Activity.MainScreen;
import in.abhishek.LibraryModule.Data.Book;
import in.abhishek.LibraryModule.Data.Borrower;
import in.abhishek.LibraryModule.Exceptions.LibraryException;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static in.abhishek.LibraryModule.Utils.AppConstants.BOOK;
import static in.abhishek.LibraryModule.Utils.AppConstants.BORROWER;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class UtilFunctions {

    public static String encrypt(String password) {

        StringBuilder passwordBuilder = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bt = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

            for (byte b : bt)
                passwordBuilder.append(String.format("%02x", b));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return passwordBuilder.toString();
    }

    /*public static Object find(String id, int type) throws LibraryException {
        if (type == BOOK) {
            ArrayList<Book> books = MainScreen.getBook();
            if (books == null) {
                MainScreen.addBooks();
            }

            books = MainScreen.getBook();

            for (Book b : books) {
                if (b.getId().equals(id)) {
                    return b;
                }
            }
            throw new LibraryException("Invalid book ID.");
        } else if (type == BORROWER) {
            ArrayList<Borrower> borrowers = MainScreen.getBorrowers();
            if (borrowers == null) {
                MainScreen.addUser();
            }

            borrowers = MainScreen.getBorrowers();
            for (Borrower b : borrowers) {
                if (b.getId().equals(id)) {
                    return b;
                }
            }
            throw new LibraryException("Invalid borrower ID.");
        }
        return null;
    }*/

    public static void println(String str) {
        System.out.println(str);
    }

    public static void updateUser(Borrower borrower){

    }
}
