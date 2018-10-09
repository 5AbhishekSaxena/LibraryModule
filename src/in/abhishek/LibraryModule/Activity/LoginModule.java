package in.abhishek.LibraryModule.Activity;

import in.abhishek.LibraryModule.Data.Book;
import in.abhishek.LibraryModule.Data.Borrower;
import in.abhishek.LibraryModule.Data.SQLiteJDBCDriverConnection;
import in.abhishek.LibraryModule.Utils.AppConstants;
import in.abhishek.LibraryModule.Utils.UtilFunctions;

import java.util.*;

import static in.abhishek.LibraryModule.Utils.UtilFunctions.println;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class LoginModule {
    private static String _ID;
    private static String password;
    private static Borrower userDetails;
    private static Book bookDetails;

    public static void main(String[] args) {
        LoginModule module = new LoginModule();
        Scanner scanner = new Scanner(System.in);
        module.setupLoginMenu(scanner);
    }

    private void setupLoginMenu(Scanner scanner) {
        println("Login Menu");
        println("1. Login");
        println("2. Sign Up");
        println("3. Exit");
        println("Enter Choice: ");
        int x = scanner.nextInt();
        switch (x) {
            case 1:
                loginMenu(scanner);
                break;
            case 2:
                signUpMenu(scanner);
                break;
            case 3:
                System.exit(0);
            case 5:
                System.out.println(new SQLiteJDBCDriverConnection().getRecords("101", AppConstants.BOOK));
                System.out.println("Book using issuedby: " + new SQLiteJDBCDriverConnection().getBookUsingUserId("2"));
                break;
            default:
                println("Invalid option selected");
                break;

        }
        setupLoginMenu(scanner);
    }

    private void signUpMenu(Scanner scanner) {
        println("Sign Up menu");
        println("1. Sign Up");
        println("2. Login");
        println("3. Back");
        int x = scanner.nextInt();
        switch (x) {
            case 1:
                if (signUp(scanner))
                    println("User Registered");
                else
                    println("User not Registered");
                break;
            case 2:
                loginMenu(scanner);
                break;
            case 3:
                System.exit(0);
                break;
            default:
                break;
        }
    }

    private boolean signUp(Scanner scanner) {
        println("SignUp Menu");
        println("Enter Name: ");
        String name = scanner.next().trim();
        println("Enter Login ID: ");
        _ID = scanner.next().trim();
        println("Enter Password: ");
        password = UtilFunctions.encrypt(scanner.next());
        return new SQLiteJDBCDriverConnection().insertRecord(AppConstants.BORROWER, _ID, name, password, "false", "-1");
    }

    private void loginMenu(Scanner scanner) {
        println("Login Menu");
        println("Enter Login ID: ");
        _ID = scanner.next().trim();
        println("Enter Password: ");
        password = UtilFunctions.encrypt(scanner.next());
        if (checkCredentials(_ID, password)) MainScreen.main(null);
        else
            println("Incorrect Login Credentials");
    }

    private boolean checkCredentials(String id, String password) {

        userDetails = (Borrower) new SQLiteJDBCDriverConnection().getRecords(id, AppConstants.BORROWER);
        if (userDetails != null) {
            String pass = userDetails.getPassword();
            if (pass.equals(password)) {
                bookDetails = new SQLiteJDBCDriverConnection().getBookUsingUserId(id);
                return true;
            }
        } else
            System.out.println("userDetails: null");
        return false;
    }

    static Borrower getUserDetails() {
        return userDetails;
    }

    static Book getBookDetails() {
        return bookDetails;
    }
}
