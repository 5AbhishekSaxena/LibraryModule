package in.abhishek.LibraryModule;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class testingClass {

    private static boolean test(String str) {
        if (str.equals("123")) {
            System.out.println("Hello123");
            return true;
        }
        return false;

    }

    public static void main(String[] args) {
        if (test("123"))
            System.out.println("sadsad");
        else
            System.out.println("failed");
    }
}
