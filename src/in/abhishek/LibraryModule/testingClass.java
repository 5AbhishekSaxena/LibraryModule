package in.abhishek.LibraryModule;

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
