package in.abhishek.LibraryModule;

import java.util.InputMismatchException;
import java.util.Scanner;

import static in.abhishek.LibraryModule.Utils.UtilFunctions.println;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class testingClass {
    public static void main(final String[] args) {
        try {
            int num = new Scanner(System.in).nextInt();
            println("num: " + num);
        }catch(InputMismatchException e){
            println("Please enter a number.");
        }
    }
}
