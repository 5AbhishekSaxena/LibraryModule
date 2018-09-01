package in.abhishek.LibraryModule.Activity;

import in.abhishek.LibraryModule.Data.Borrower;
import in.abhishek.LibraryModule.Data.SessionManager;
import in.abhishek.LibraryModule.Exceptions.LibraryException;
import in.abhishek.LibraryModule.Utils.UtilFunctions;

import java.util.Scanner;

import static in.abhishek.LibraryModule.Utils.AppConstants.BORROWER;
import static in.abhishek.LibraryModule.Utils.UtilFunctions.find;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class LoginModule {

    private static Scanner scanner;
    private static String _ID;
    private static String password;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        setupLoginMenu();
    }

    private static void setupLoginMenu() {
        System.out.println("Login Menu");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.println("3. Exit - not working");
        System.out.println("Enter Choice: ");
        int x = scanner.nextInt();
        switch (x) {
            case 1:
                loginMenu();
                break;
            case 2:
                signUpMenu();
                break;
            default:
                System.out.println("Invalid option selected");
                break;

        }
        setupLoginMenu();
    }

    private static void signUpMenu() {

    }

    private static void loginMenu() {
        System.out.println("Login Menu");
        System.out.println("Enter Login ID: ");
        _ID = scanner.next();
        System.out.println("Enter Password: ");
        password = UtilFunctions.encrypt(scanner.next());
        if (checkCredentials(_ID, password)){
            //SessionManager.login();
            MainScreen.main(null);
        }
        else
            System.out.println("Incorrect Login Credentials");
    }

    private static boolean checkCredentials(String id, String password) {
        try {
            Borrower currentBorrower = (Borrower) find(id, BORROWER);

            if (currentBorrower != null) {
                if (currentBorrower.getPassword().equals(password)) {
                    return true;
                }
            }
        } catch (LibraryException e) {
            return false;
        }
        return false;
    }
}
