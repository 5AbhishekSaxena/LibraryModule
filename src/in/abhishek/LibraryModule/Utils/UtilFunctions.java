package in.abhishek.LibraryModule.Utils;

import java.io.Console;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

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

    public void passwordExample() {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            System.exit(0);
        }
        char passwordArray[] = console.readPassword("Enter your secret password: ");
        console.printf("Password entered was: %s%n", new String(passwordArray));

    }

    public static void println(String str) {
        System.out.println(str);
    }
}
